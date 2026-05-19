const ActivityFeed = ()=>{

const activities=[

{
id:1,
text:"Spring Boot request accepted"
},

{
id:2,
text:"React skill added"
},

{
id:3,
text:"Completed Java exchange"
}

];

return(

<div
className="activity-card"
>

<h3>

Recent Activity

</h3>

{

activities.map(

(item)=>(

<div
key={item.id}
className="activity-item"
>

{item.text}

</div>

)

)

}

</div>

);

};

export default ActivityFeed;