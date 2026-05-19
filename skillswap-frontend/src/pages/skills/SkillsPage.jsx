import {
useEffect,
useState
}
from "react";

import SkillCard
from "../../components/skills/SkillCard";

import SkillFilters
from "../../components/skills/SkillFilters";

import {

getAllSkills,
searchSkills

}
from "../../api/skillApi";

const SkillsPage=()=>{

const [skills,setSkills]=
useState([]);

const [search,setSearch]=
useState("");

useEffect(()=>{

loadSkills();

},[]);


useEffect(()=>{

const timer=

setTimeout(()=>{

if(search){

searchSkill();

}

else{

loadSkills();

}

},500);

return()=>clearTimeout(
timer
);

},[search]);


const loadSkills=
async()=>{

const data=
await getAllSkills();

setSkills(data);

};


const searchSkill=
async()=>{

const data=

await searchSkills(
search
);

setSkills(data);

};

return(

<div
className="skills-page"
>

<h1>

Discover Skills

</h1>

<SkillFilters

search={search}

setSearch={setSearch}

/>

<div
className="skills-grid"
>

{

skills.map(

(skill)=>(

<SkillCard

key={skill.id}

name={skill.skillName}

level={skill.level}

type={skill.type}

description={skill.description}

/>

)

)

}

</div>

</div>

);

};

export default SkillsPage;