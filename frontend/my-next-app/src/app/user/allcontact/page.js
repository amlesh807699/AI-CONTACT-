"use client";

import axios from "axios";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import styles from "./page.module.css";

export default function AllContactsPage() {
  const [contacts, setContacts] = useState([]);
  const [editingContact, setEditingContact] = useState(null);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    company: "",
    jobTitle: "",
    city: "",
  });

  const router = useRouter();

  
  useEffect(() => {
    fetchContacts();
  }, []);

  const fetchContacts = async () => {
    try {
      const res = await axios.get("http://localhost:8080/user/contacts", {
        withCredentials: true,
      });
      setContacts(res.data);
    } catch (err) {
      console.error(err);
      alert("Failed to load contacts ");
    }
  };

  
  const openEditForm = (c) => {
    setEditingContact(c);
    setFormData({
      name: c.name,
      email: c.email,
      phone: c.phone,
      company: c.company,
      jobTitle: c.jobTitle,
      city: c.city,
    });
  };

  
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  
  const submitUpdate = async (e) => {
    e.preventDefault();

    try {
      await axios.put(
        `http://localhost:8080/user/contacts/${editingContact.id}`,
        formData,
        { withCredentials: true }
      );

      alert("Contact updated ");
      setEditingContact(null);
      fetchContacts();
    } catch (err) {
      console.error(err);
      alert(err.response?.data || "Update failed ");
    }
  };

  
  const deleteContact = async (id) => {
    if (!confirm("Delete this contact?")) return;

    try {
      await axios.delete(`http://localhost:8080/user/contacts/${id}`, {
        withCredentials: true,
      });
      setContacts((prev) => prev.filter((c) => c.id !== id));
    } catch (err) {
      console.error(err);
      alert("Delete failed ");
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>All Contacts </h2>

      {contacts.map((c) => (
        <div key={c.id} className={styles.card}>
          <p><b>Name:</b> {c.name}</p>
          <p><b>Phone:</b> {c.phone}</p>
        

          <button
            className={styles["details-btn"]}
            onClick={() => router.push(`/user/allcontact/${c.id}`)}
          >
            Details
          </button>
          <button
            className={styles["edit-btn"]}
            onClick={() => openEditForm(c)}
          >
            Edit
          </button>
          <button
            className={styles["delete-btn"]}
            onClick={() => deleteContact(c.id)}
          >
            Delete
          </button>
        </div>
      ))}

    
      {editingContact && (
        <div className={styles.modal}>
          <form onSubmit={submitUpdate} className={styles["modal-form"]}>
            <h3>Edit Contact</h3>

            <input
              name="name"
              value={formData.name}
              onChange={handleChange}
            />
            <input
              name="email"
              value={formData.email}
              disabled
            />
            <input
              name="phone"
              value={formData.phone}
              onChange={handleChange}
            />
            <input
              name="company"
              value={formData.company}
              onChange={handleChange}
            />
            <input
              name="jobTitle"
              value={formData.jobTitle}
              onChange={handleChange}
            />
            <input
              name="city"
              value={formData.city}
              onChange={handleChange}
            />

            <button type="submit" className={styles["save-btn"]}>Save</button>
            <button
              type="button"
              className={styles["cancel-btn"]}
              onClick={() => setEditingContact(null)}
            >
              Cancel
            </button>
          </form>
        </div>
      )}
    </div>
  );
}
