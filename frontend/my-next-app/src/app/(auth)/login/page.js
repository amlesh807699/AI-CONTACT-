"use client";

import styles from "./page.module.css";
import { useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      await axios.post(
        "http://localhost:8080/auth/login",
        { email, password },
        { withCredentials: true }
      );

    
      const me = await axios.get(
        "http://localhost:8080/auth/me",
        { withCredentials: true }
      );

      const { role, email: userEmail } = me.data;

      if (!role) {
        throw new Error("Invalid auth response");
      }

    
      localStorage.setItem("role", role);
      localStorage.setItem("email", userEmail);

      
      window.dispatchEvent(new Event("roleChange"));

      
      if (role === "ADMIN") router.push("/admin/dashboard");
      else if (role === "USER") router.push("/user/dashboard");
      else router.push("/");

    } catch (err) {
      if (!err.response) {
        throw new Error("Server is not reachable");
      }

      if (err.response.status === 401) {
        setMessage(" Invalid email or password");
        return;
      }

      if (err.response.status === 403) {
        setMessage("Access denied");
        return;
      }

      throw new Error("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Welcome Back </h2>

      <form onSubmit={handleSubmit} className={styles.form}>
        <input
          type="email"
          placeholder="Email address"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          className={styles.input}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className={styles.input}
        />

        <button
          type="submit"
          disabled={loading}
          className={styles.button}
        >
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>

      {message && (
        <p className={styles.error}>
          {message}
        </p>
      )}
    </div>
  );
}
