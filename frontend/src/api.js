import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/game';

export const getGraph = async () => {
  const response = await axios.get(`${API_BASE}/graph`);
  return response.data;
};

export const setDirection = async (id) => {
  await axios.post(`${API_BASE}/direction?id=${id}`);
};

export const setColor = async (color) => {
  await axios.post(`${API_BASE}/color?color=${encodeURIComponent(color)}`);
};

export const setEngine = async (engineType) => {
  await axios.post(`${API_BASE}/engine?engineType=${engineType}`);
};

export const setSpeed = async (speed) => {
  await axios.post(`${API_BASE}/speed?speed=${speed}`);
};

export const saveGame = async (path) => {
  await axios.post(`${API_BASE}/save?path=${encodeURIComponent(path)}`);
};

export const loadGame = async (path) => {
  await axios.post(`${API_BASE}/load?path=${encodeURIComponent(path)}`);
};
