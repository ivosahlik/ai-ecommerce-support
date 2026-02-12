import React from "react";
import HeroLogo from "../hero/HeroLogo";
import HeroAnim from "../hero/HeroAnim";
import NavBar from "./NavBar";

const Header = () => {
  return (
    <section className='hero-section'>
      <HeroLogo />
      <div
        style={{
          flex: "1 1 320px",
          display: "flex",
          justifyContent: "center",
        }}>
        <HeroAnim />
      </div>
      <NavBar />
    </section>
  );
};

export default Header;
