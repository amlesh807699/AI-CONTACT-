import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 10,
  duration: "30s",
};

export default function () {
  const payload = JSON.stringify({
    name: `user${__VU}_${__ITER}`,
    email: `user${__VU}_${__ITER}@test.com`,
    password: "12345556"
  });

  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  const res = http.post(
    "https://backend-production-983d.up.railway.app/auth/register",
    payload,
    params
  );

  check(res, {
    "status is 201": (r) => r.status === 201,
  });

  console.log(`Status: ${res.status}`);
  sleep(1);
}