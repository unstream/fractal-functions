import http from 'k6/http';

export const options = {
  scenarios: {
    test: {
      executor: "shared-iterations",
      exec: "test",
      vus: 2,
      iterations: 100,
    },
  },
};

export function test() {
  let fractal = {
    "c0": -1.5,
    "c0i": -1,
    "c1": 0.5,
    "c1i": 1,
    "width": 100,
    "height": 100,
    "maxIterations": 100
  };
  // Using a JSON string as body
  let res = http.post(`https://fractal-gateway-8191wl1t.nw.gateway.dev/fastMandelbrot?key=${__ENV.API_KEY}`, JSON.stringify(fractal), {
    headers: { 'Content-Type': 'image/png' },
  });
}
