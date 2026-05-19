const FeaturesSection=()=>{

const features=[

{
title:"Skill Matching",
desc:"Find people based on skills"
},

{
title:"Exchange Requests",
desc:"Send and manage requests"
},

{
title:"Progress Tracking",
desc:"Track your learning journey"
},

{
title:"Community Driven",
desc:"Learn directly from people"
}

];

return(

<section className="features">

<h2>

Why SkillSwap

</h2>

<div className="feature-grid">

{

features.map(

(item,index)=>(

<div
key={index}
className="feature-card"
>

<h3>

{item.title}

</h3>

<p>

{item.desc}

</p>

</div>

)

)

}

</div>

</section>

);

};

export default FeaturesSection;