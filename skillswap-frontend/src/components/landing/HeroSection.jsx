import { useNavigate } from "react-router-dom";
import Button from "../ui/Button";

const HeroSection=()=>{

const navigate=
useNavigate();

return(

<section className="hero-v2">

<div className="hero-content">

<div className="hero-badge">

🚀 Skill Exchange Platform

</div>

<h1>

Learn Faster Through

<span>

 Real People

</span>

</h1>

<p>

Connect with learners and experts.
Teach what you know and gain skills
through meaningful collaboration.

</p>

<div className="hero-actions">

<Button
onClick={()=>navigate("/signup")}
>

Get Started

</Button>

<Button
variant="secondary"
onClick={()=>navigate("/login")}
>

Explore

</Button>

</div>

</div>


<div className="hero-preview">

<div className="floating-card">

<h3>

Spring Boot ↔ UI Design

</h3>

<p>

Active exchange session

</p>

</div>

<div className="floating-card">

<h3>

React ↔ DSA

</h3>

<p>

12 requests today

</p>

</div>

<div className="floating-card">

<h3>

Java ↔ Cloud

</h3>

<p>

Trending skill swap

</p>

</div>

</div>

</section>

);

};

export default HeroSection;