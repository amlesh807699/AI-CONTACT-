"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import styles from "./page.module.css";

const VerifyPage = () => {
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  // ✅ get email from localStorage
  useEffect(() => {
    const storedEmail = localStorage.getItem("verifyEmail");
    if (!storedEmail) {
      setMessage("❌ Email not found. Please register again.");
      return;
    }
    setEmail(storedEmail);
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");
    setSuccess(false);

    try {
      const res = await fetch(
        `http://localhost:8080/auth/verify?email=${email}&otp=${otp}`,
        { method: "POST" }
      );

      if (res.status >= 500) throw new Error("OTP service failed");

      const data = await res.text();
      if (!res.ok) {
        setMessage(data || "❌ Invalid OTP");
        return;
      }

      // ✅ Success
      localStorage.removeItem("verifyEmail");
      setMessage("✅ OTP verified successfully");
      setSuccess(true);

      setTimeout(() => router.push("/login"), 2000);
    } catch (err) {
      setMessage("Unable to verify OTP ❌");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Verify OTP 🔒</h2>

      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="email"
          value={email}
          readOnly
          className={styles.input}
        />

        <input
          type="number"
          placeholder="Enter OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          required
          className={styles.input}
        />

        <button type="submit" disabled={loading} className={styles.button}>
          {loading ? "Verifying..." : "Verify"}
        </button>
      </form>

      {message && (
        <p className={`${styles.message} ${success ? styles.success : ""}`}>
          {message}
        </p>
      )}
    </div>
  );
};

export default VerifyPage;
