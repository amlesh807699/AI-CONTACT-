"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";
import styles from "./page.module.css";

const Page = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await axios.get("http://localhost:8080/admin/users", {
          withCredentials: true,
        });
        const usersData = Array.isArray(res.data)
          ? res.data
          : res.data.users || res.data.data || res.data.content || [];
        setUsers(usersData);
      } catch (err) {
        if (axios.isAxiosError(err)) {
          if (err.response?.status === 401) setError("Unauthorized. Please login.");
          else if (err.response?.status === 403) setError("Forbidden. Admin access only.");
          else setError("Something went wrong.");
        } else {
          setError("Network error.");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUsers();
  }, []);

  const goToDetails = (id) => {
    router.push(`/admin/alluser/${id}`);
  };

  if (loading) return <p>Loading users...</p>;

  return (
    <div className={styles.container}>
      <h1 className={styles.heading}>All Users</h1>
      {error && <p className={styles.error}>{error}</p>}
      <ul className={styles.userList}>
        {users.map((user) => (
          <li key={user.id} onClick={() => goToDetails(user.id)}>
            {user.name} — {user.email} — <b>{user.role}</b>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Page;
