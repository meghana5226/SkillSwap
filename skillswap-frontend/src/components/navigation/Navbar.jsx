import {
Link
}
from "react-router-dom";

const Navbar=()=>{

return(

<nav
className="navbar"
>

<div
className="logo"
>

SkillSwap

</div>


<div
className="nav-links"
>

<Link to="/">

Home

</Link>

<Link to="/login">

Login

</Link>

<Link to="/signup">

Signup

</Link>

</div>

</nav>

);

};

export default Navbar;