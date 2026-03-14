"use client";

export default function Error({ error, reset }) {
  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      <h1>💥 Something went wrong</h1>
      <p>{error.message}</p>
      <button onClick={() => reset()}>Try Again</button>
    </div>
  );
}
