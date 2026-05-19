import {

Link,
useLocation

}

from "react-router-dom";

const Sidebar=()=>{

const location=
useLocation();

const menus=[

{
name:"Dashboard",
path:"/dashboard"
},

{
name:"Profile",
path:"/profile"
},

{
name:"Skills",
path:"/skills"
},

{
name:"Requests",
path:"/requests"
},

{
name:"Notifications",
path:"/notifications"
}

];

return(

<div
className="sidebar"
>

<h1>

SkillSwap

</h1>

<div
className="sidebar-links"
>

{

menus.map(

(item)=>(

<Link

key={item.path}

to={item.path}

className={

location.pathname===item.path

?

"active-menu"

:

""

}

>

{item.name}

</Link>

)

)

}

</div>

</div>

);

};

export default Sidebar;