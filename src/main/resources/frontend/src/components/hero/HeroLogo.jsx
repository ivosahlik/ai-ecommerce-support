import React from "react";
import { Link } from "react-router-dom";

const HeroLogo = () => {
  return (
    <div
      style={{
        flex: "1 1 350px",
        maxWidth: 500,
      }}>
      <h1 className='hero-title'>
        Welcome to AI <span>Ecommerce Support</span>
      </h1>
      <p className='hero-description'>
        We’re here for you 24/7. Chat with our friendly team, get instant help,
        or browse our knowledge base.
      </p>
      <Link to={"/chat"} className='start-chat-button'>
        Chat With Our Support
      </Link>
    </div>
  );
};

export default HeroLogo;
