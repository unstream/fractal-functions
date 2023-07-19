import http from 'k6/http';

export const options = {
  scenarios: {
    test: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        {target: 100, duration: '5m'},
        {target: 100, duration: '5m'},
      ],
      gracefulRampDown: '0s',
    }
  }
};

export default function () {
  let fractal = {
    "c0": -1.5,"c0i": -1,
    "c1": 0.5, "c1i": 1,
    "width": 300,"height": 300, "maxIterations": 300
  };
  let host = 'https://fractal-gateway-8191wl1t.nw.gateway.dev';
  let res = http.post(host + `/fastMandelbrot?key=${__ENV.API_KEY}`,
      JSON.stringify(fractal), {
    headers: { 'Content-Type': 'image/png' },
  });
}
