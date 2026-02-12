import React, { useState } from "react";
import Tickets from "./Tickets";
import Sidebar from "./Sidebar";

const AdminDashboard = () => {
  const [activeMenu, setActiveMenu] = useState("tickets");
  const [searchRef, setSearchRef] = useState("");

  const handleSearch = (refNumber) => {
    setSearchRef(refNumber);
    setActiveMenu("tickets");
  };

  return (
    <div className='admin-container'>
      <Sidebar
        activeMenu={activeMenu}
        setActiveMenu={setActiveMenu}
        onSearch={handleSearch}
      />

      <main className='admin-main-content mt-4'>
        {activeMenu === "tickets" && (
          <section>
            <h1>Tickets</h1>
            <Tickets searchRef={searchRef} />
          </section>
        )}
        {activeMenu === "conversations" && (
          <section>
            <h1>conversations</h1>
          </section>
        )}
      </main>
    </div>
  );
};

export default AdminDashboard;
