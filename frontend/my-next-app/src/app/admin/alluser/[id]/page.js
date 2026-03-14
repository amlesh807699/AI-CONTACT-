"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import axios from "axios";
import styles from "./page.module.css";

const Page = () => {
  const params = useParams();
  const userId = params.id;
  const router = useRouter();

  const [user, setUser] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/admin/users/${userId}`,
          { withCredentials: true }
        );
        setUser(res.data);
      } catch (err) {
        if (axios.isAxiosError(err)) {
          if (err.response?.status === 404) setError("User not found.");
          else if (err.response?.status === 401) setError("Unauthorized.");
          else setError("Something went wrong.");
        } else {
          setError("Network error.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [userId]);

  const handleDelete = async () => {
    if (!confirm("Are you sure you want to delete this user?")) return;

    try {
      setDeleting(true);
      await axios.delete(`http://localhost:8080/admin/users/${userId}`, {
        withCredentials: true,
      });
      alert("User deleted successfully");
      router.push("/admin/users");
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 401) setError("Unauthorized.");
        else setError("Something went wrong while deleting.");
      } else {
        setError("Network error.");
      }
      setDeleting(false);
    }
  };

  if (loading) return <p>Loading user info...</p>;
  if (error) return <p className={styles.error}>{error}</p>;
  if (!user) return null;

  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>User Details</h1>
      <div className={styles.userDetails}>
        <p><strong>ID:</strong> {user.id}</p>
        <p><strong>Name:</strong> {user.name}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Role:</strong> {user.role}</p>
        <p><strong>Verified:</strong> {user.verified ? "Yes" : "No"}</p>
        <p><strong>Flagged:</strong> {user.flagged ? "Yes" : "No"}</p>
        {user.flagged_reason && <p><strong>Flagged Reason:</strong> {user.flagged_reason}</p>}
      </div>
      <button
        className={styles.deleteButton}
        onClick={handleDelete}
        disabled={deleting}
      >
        {deleting ? "Deleting..." : "Delete User"}
      </button>
    </div>
  );
};

export default Page;
