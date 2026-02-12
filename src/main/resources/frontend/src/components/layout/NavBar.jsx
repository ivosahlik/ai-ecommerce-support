import React, {useState} from "react";
import { useLocation, Link } from "react-router-dom";

const NavBar = () => {
  const location = useLocation();
  const [activeItem, setActiveItem] = useState(location.pathname);

  const handleItemClick = (path) => {
    setActiveItem(path);
  };

  const navItems = [
    { path: "/", label: "Home" },
    { path: "/chat", label: "Chat" },
    { path: "/tickets", label: "Tickets" },
  ];

  return (
    <nav className='nav-container'>
      <ul className='nav-list'>
        {navItems.map((item) => (
          <li className='nav-item' key={item.path}>
            <Link
              to={item.path}
              className={activeItem === item.path ? "active" : ""}
              onClick={() => handleItemClick(item.path)}>
              {item.label}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default NavBar;
