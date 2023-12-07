import http from 'k6/http';

export const options = {
  scenarios: {
    test: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        {target: 100, duration: '1m'},
        {target: 100, duration: '1m'},
      ],
      gracefulRampDown: '0s',
    }
  }
};

export default function () {
  let fractal = {
    "c0": -1.5,"c0i": -1,
    "c1": 0.5, "c1i": 1,
    "width": 1000,"height": 1000, "maxIterations": 50
  };
  let host = 'http://localhost:8084';

  let res = http.post(host + `/computeMandelbrotImage?key=${__ENV.API_KEY}`,
      JSON.stringify(fractal), {
    headers: { 'Content-Type': 'image/png', 'x-goog-api-key': '${__ENV.API_KEY}'},
  });
}
