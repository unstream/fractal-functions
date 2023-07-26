import http from 'e2e/k6/http';

export const options = {
  scenarios: {
    test: {
      executor: "shared-iterations",
      exec: "test",
      vus: 20,
      iterations: 1000,

    },
  },
};

export function test() {
  let fractal = {
    "c0": -1.5,
    "c0i": -1,
    "c1": 0.5,
    "c1i": 1,
    "width": 300,
    "height": 300,
    "maxIterations": 300
  };
  // Using a JSON string as body
  let res = http.post(`https://fractal-gateway-8191wl1t.nw.gateway.dev/fastMandelbrot?key=${__ENV.API_KEY}`, JSON.stringify(fractal), {
  //  let res = http.post(`http://localhost:8084/fastMandelbrot`, JSON.stringify(fractal), {
    headers: { 'Content-Type': 'image/png' },
  });
}
