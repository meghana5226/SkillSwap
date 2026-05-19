const SkillFilters=({

search,
setSearch

})=>{

return(

<div
className="skill-filters"
>

<input

placeholder="Search skills..."

value={search}

onChange={(e)=>

setSearch(
e.target.value
)

}

/>

</div>

);

};

export default SkillFilters;