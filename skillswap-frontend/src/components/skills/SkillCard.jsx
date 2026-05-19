const SkillCard=({

name,
level,
type,
description

})=>{

return(

<div className="skill-card">

<div className="skill-top">

<h3>

{name}

</h3>

<span>

{level}

</span>

</div>

<p>

{description}

</p>

<div className="skill-type">

{type}

</div>

</div>

);

};

export default SkillCard;