import Button from "../ui/Button";
import { useNavigate } from "react-router-dom";

const CTASection=()=>{

const navigate=
useNavigate();

return(

<section className="cta">

<h2>

Start exchanging skills today

</h2>

<Button
onClick={()=>navigate("/signup")}
>

Join SkillSwap

</Button>

</section>

);

};

export default CTASection;