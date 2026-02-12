import React from "react";
import SupportTeam from "../team/SupportTeam";


const Home = () => {
  return (
    <div className='body'>
      <section className='team-section' id='support'>
        <div style={{ display: "flex" }}>
          <SupportTeam />
        </div>
      </section>
    </div>
  );
};

export default Home;
