
import Slider from "react-slick";
import { supportTeam } from './supportTeamData';


const SupportTeam = () => {

    const settings = {
      dots: true,
      infinite: true,
      speed: 10000,
      autoplay: true,
      autoplaySpeed: 3000,
      slidesToShow: 3,
    };

  return (
    <section className='team-section mb-5' id='support'>
      <h2 className='team-title'>Meet Our Support Team</h2>
      <Slider {...settings}>
        {supportTeam.map((member) => (
          <div key={member.name} className='team-member'>
            <img src={member.img} alt={member.name} />
            <div>
              <span className='member-name'>{member.name}</span>
              <br />
              <span className='member-role'>{member.role}</span>
            </div>
          </div>
        ))}
      </Slider>
    </section>
  );
}

export default SupportTeam



