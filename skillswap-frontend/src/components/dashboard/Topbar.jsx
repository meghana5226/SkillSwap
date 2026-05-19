import useAuth
from "../../hooks/useAuth";

const Topbar=()=>{

const {user}=useAuth();

return(

<header
className="topbar"
>

<div>

<h2>

Welcome back,

{user?.name || "User"}

👋

</h2>

<p>

Build skills through collaboration

</p>

</div>


<div
className="profile-mini"
>

<img

src="https://ui-avatars.com/api/?name=User"

alt="profile"

/>

</div>

</header>

);

};

export default Topbar;