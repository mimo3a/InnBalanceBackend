/**
 * Statistics Service
 *
 * Manages breathing exercise session data using AsyncStorage.
 * Provides functions to save, retrieve, and clear session statistics.
 *
 * Each session contains:
 * - id: Unique identifier (timestamp-based)
 * - duration: Session length in seconds
 * - date: ISO timestamp of when session occurred
 * - state: User's mood/state during the session
 *
 * TODO: Consider migrating to TypeScript for better type safety and session shape enforcement
 */

import AsyncStorage from '@react-native-async-storage/async-storage';

// Storage key for AsyncStorage
const STORAGE_KEY = 'breathing_sessions';

// Queue to prevent concurrent write operations
let writeQueue = Promise.resolve();

/**
 * Generate a unique ID for a session
 * Uses timestamp-based approach for simplicity
 */
function generateSessionId() {
  return `session_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}

/**
 * Ensure date is in ISO string format
 */
function normalizeDate(date) {
  if (date instanceof Date) return date.toISOString();
  if (typeof date === 'number') return new Date(date).toISOString();
  if (typeof date === 'string') {
    const d = new Date(date);
    if (!isNaN(d.getTime())) return d.toISOString();
  }
  return new Date().toISOString();
}

/**
 * Defensive clone
 */
function clone(obj) {
  return JSON.parse(JSON.stringify(obj));
}

/**
 * Validate and normalize session fields
 */
function normalizeSessionInput(session) {
  const normalized = {
    id: session.id || generateSessionId(),
    duration: Number(session.duration) || 0,
    date: normalizeDate(session.date || new Date()),
    state: session.state ?? null,
    ...session
  };
  // Ensure date and duration types are correct
  normalized.duration = Number(normalized.duration) || 0;
  normalized.date = normalizeDate(normalized.date);
  return normalized;
}

/**
 * Enqueue a write operation but ensure the global queue never stays rejected.
 * Returns the result promise for callers while protecting subsequent writes.
 */
function enqueueWrite(fn) {
  // resultPromise may reject, but we attach a swallow-catcher to writeQueue to
  // keep the chain alive for future operations.
  const resultPromise = writeQueue.then(() => fn());
  // Ensure writeQueue becomes a promise that never rejects (swallow errors)
  writeQueue = resultPromise.catch((err) => {
    console.error('Queued write failed:', err);
    // swallow so next queued write still runs
  });
  return resultPromise;
}

/**
 * Save a new breathing session to storage
 * Appends the session to existing sessions array
 * Uses a queue to prevent race conditions
 *
 * @param {Object} session - Session object with duration, date, and state
 * @returns {Promise<Object>} Saved session with generated ID
 */
export function saveSession(session) {
  return enqueueWrite(async () => {
    try {
      const sessionWithId = normalizeSessionInput(session);

      const existing = await AsyncStorage.getItem(STORAGE_KEY);
      const sessions = existing ? JSON.parse(existing) : [];

      // Add new session to array
      sessions.push(sessionWithId);

      // Save updated array back to storage
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(sessions));

      return clone(sessionWithId);
    } catch (error) {
      console.error('Failed to save session', error);
      throw error;
    }
  });
}

/**
 * Retrieve all saved breathing sessions
 * Returns a deep-cloned array to avoid external mutation
 */
export async function getSessions() {
  try {
    const data = await AsyncStorage.getItem(STORAGE_KEY);
    const parsed = data ? JSON.parse(data) : [];
    return clone(parsed);
  } catch (error) {
    console.error('Failed to load sessions', error);
    return [];
  }
}

/**
 * Clear all session statistics from storage
 * Uses a queue to prevent race conditions with concurrent saves
 */
export function clearSessions() {
  return enqueueWrite(async () => {
    try {
      await AsyncStorage.removeItem(STORAGE_KEY);
    } catch (error) {
      console.error('Failed to clear sessions', error);
      throw error;
    }
  });
}

/**
 * Get an aggregated statistics summary
 * Returns totalSessions, totalDuration, averageDuration, mostCommonState
 */
export async function getStatisticsSummary() {
  const sessions = await getSessions();
  const totalSessions = sessions.length;
  const totalDuration = sessions.reduce((sum, s) => sum + (Number(s.duration) || 0), 0);
  const averageDuration = totalSessions ? totalDuration / totalSessions : 0;

  const stateCounts = sessions.reduce((map, s) => {
    const k = s.state ?? 'unknown';
    map[k] = (map[k] || 0) + 1;
    return map;
  }, {});

  const mostCommonState = Object.entries(stateCounts)
    .sort((a, b) => b[1] - a[1])
    [0]?.[0] || null;

  return {
    totalSessions,
    totalDuration,
    averageDuration,
    mostCommonState
  };
}

/**
 * Get sessions in a date period (inclusive)
 * start and end can be Date, ISO string, or timestamp
 */
export async function getSessionsByPeriod(start, end) {
  const s = start ? new Date(start) : new Date(0);
  const e = end ? new Date(end) : new Date();
  if (isNaN(s.getTime()) || isNaN(e.getTime())) return [];

  const sessions = await getSessions();
  return sessions.filter(ss => {
    const d = new Date(ss.date);
    return d >= s && d <= e;
  });
}

/**
 * Get last N sessions ordered by date descending
 */
export async function getLastNSessions(n = 10) {
  const sessions = await getSessions();
  const sorted = sessions.slice().sort((a, b) => new Date(b.date) - new Date(a.date));
  return sorted.slice(0, n);
}

/**
 * Delete a session by id
 * Returns true if a session was deleted, false otherwise
 */
export function deleteSessionById(id) {
  return enqueueWrite(async () => {
    try {
      const sessions = await getSessions();
      const filtered = sessions.filter(s => s.id !== id);
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(filtered));
      return filtered.length !== sessions.length;
    } catch (error) {
      console.error('Failed to delete session', error);
      throw error;
    }
  });
}

/**
 * Update a session by id with a patch object
 * Returns the updated session (or null if not found)
 */
export function updateSessionById(id, patch) {
  return enqueueWrite(async () => {
    try {
      const sessions = await getSessions();
      let found = null;
      const updated = sessions.map(s => {
        if (s.id !== id) return s;
        const merged = { ...s, ...patch };
        // normalize important fields
        merged.duration = Number(merged.duration) || 0;
        merged.date = normalizeDate(merged.date || s.date);
        found = merged;
        return merged;
      });
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(updated));
      return found ? clone(found) : null;
    } catch (error) {
      console.error('Failed to update session', error);
      throw error;
    }
  });
}

export default {
  saveSession,
  getSessions,
  clearSessions,
  getStatisticsSummary,
  getSessionsByPeriod,
  getLastNSessions,
  deleteSessionById,
  updateSessionById
};
