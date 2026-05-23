const UserSkillList = ({skills=[]}) => {

return(

<div className="user-skills">

<h2>

Skills

</h2>

<div className="skill-tags">

{

skills.map((skill)=>(

<span
key={skill.id}
className="tag"
>

{skill.skillName}

</span>

))

}

</div>

</div>

);

};

export default UserSkillList;