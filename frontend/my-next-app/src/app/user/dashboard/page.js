"use client";

import axios from "axios";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "./page.module.css";

const Dashboard = () => {
  const router = useRouter();

  const [user, setUser] = useState(null);
  const [uiError, setUiError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await axios.get("http://localhost:8080/auth/me", {
          withCredentials: true,
        });

        setUser(res.data);
      } catch (err) {
        const status = err.response?.status;

        if (status === 401) {
          setUiError("Unauthorized. Please login.");
          router.push("/login");
        } else {
          setUiError("Failed to load user data.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [router]);

  const handleLogout = () => {
    // Clear cookies on backend if needed
    router.push("/login");
  };

  if (loading) {
    return <p className={styles.loadingText}>Loading...</p>;
  }

  if (uiError) {
    return (
      <div className={styles.errorContainer}>
        <h2>{uiError}</h2>
      </div>
    );
  }

  return (
    <div className={styles.dashboardContainer}>
      <h1>Welcome, {user.name}</h1>
      <p>Email: {user.email}</p>

      <div className={styles.cardGrid}>
        <div className={styles.card} onClick={() => router.push("/user/addcontact")}>
          <h3>add contactt</h3>
          <p>add your contact </p>
        </div>
        <div className={styles.card} onClick={() => router.push("/user/allcontact")}>
          <h3>Contacts</h3>
          <p>View all your contacts</p>
        </div>
        <div className={styles.card} onClick={() => router.push("/user/search")}>
          <h3>Search</h3>
          <p>Search contacts</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
