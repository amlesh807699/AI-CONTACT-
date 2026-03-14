"use client";

export default function Error({ error, reset }) {
  return (
    <div
      style={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        background: "#f8f8f8",
      }}
    >
      <h1 style={{ fontSize: "32px", color: "red" }}>
         Something went wrong
      </h1>

      <p style={{ marginTop: "10px", color: "#333" }}>
        {error.message}
      </p>

      <button
        style={{
          marginTop: "20px",
          padding: "10px 20px",
          cursor: "pointer",
        }}
        onClick={() => reset()}
      >
        Try Again
      </button>
    </div>
  );
}
