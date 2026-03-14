"use client";

export default function Error({ error, reset }) {
  return (
    <div style={{ padding: 40, textAlign: "center" }}>
      <h1>Something went wrong 😢</h1>
      <p style={{ color: "red" }}>{error.message}</p>

      <button
        onClick={reset}
        style={{
          marginTop: 20,
          padding: "10px 20px",
          background: "#0070f3",
          color: "white",
          border: "none",
          borderRadius: "5px",
        }}
      >
        Try Again
      </button>
    </div>
  );
}
