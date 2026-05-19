const StatCard = ({
    title,
    value,
    icon,
    trend
}) => {

return(

<div className="stat-card">

<div className="stat-header">

<div>

<h4>
{title}
</h4>

<h2>
{value}
</h2>

</div>

<div
className="stat-icon"
>

{icon}

</div>

</div>

<p>

{trend}

</p>

</div>

);

};

export default StatCard;