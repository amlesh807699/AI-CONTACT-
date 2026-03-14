"use client";

export default function Error({ error, reset }) {
  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        fontFamily: "Arial, sans-serif",
        padding: "40px",
        textAlign: "center",
      }}
    >
      <h1 style={{ color: "red", marginBottom: "10px" }}>
        Something went wrong 😢
      </h1>

      <p style={{ color: "#555", maxWidth: "500px" }}>
        {error?.message || "Unexpected system error occurred."}
      </p>

      <div style={{ marginTop: "30px", display: "flex", gap: "15px" }}>
        <button
          onClick={() => reset()}
          style={buttonStyle}
        >
          Try Again
        </button>

        <button
          onClick={() => window.location.href = "/login"}
          style={{ ...buttonStyle, background: "red" }}
        >
          Go to Login
        </button>
      </div>
    </div>
  );
}

const buttonStyle = {
  padding: "10px 20px",
  background: "#0070f3",
  color: "white",
  border: "none",
  borderRadius: "6px",
  cursor: "pointer",
};
