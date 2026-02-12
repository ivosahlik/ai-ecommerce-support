import React from "react";
import Searchbar from "./Searchbar";

const Sidebar = ({ activeMenu, setActiveMenu, onSearch }) => {
  return (
    <aside className='admin-sidebar'>
      <h2 className='admin-tilte'>Admin Dashboard</h2>
      <nav className='mb-5'>
        <ul className='admin-menu'>
          <li
            className={`admin-menu-item ${
              activeMenu === "tickets" ? "active" : ""
            }`}
            onClick={() => setActiveMenu("tickets")}>
            Tickets
          </li>

          <li
            className={`admin-menu-item ${
              activeMenu === "conversations" ? "active" : ""
            }`}
            onClick={() => setActiveMenu("conversations")}>
            Conversations
          </li>
        </ul>
      </nav>
      {/* The search bar is coming here... */}
      <Searchbar onSearch={onSearch} />
    </aside>
  );
};

export default Sidebar;
