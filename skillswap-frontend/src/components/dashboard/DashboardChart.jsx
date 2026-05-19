import {

ResponsiveContainer,
LineChart,
Line,
XAxis,
YAxis,
Tooltip

}
from "recharts";

const DashboardChart = ({
data
})=>{

return(

<div className="chart-card">

<h3>

Skill Activity

</h3>

<ResponsiveContainer
width="100%"
height={300}
>

<LineChart
data={data}
>

<XAxis
dataKey="month"
/>

<YAxis/>

<Tooltip/>

<Line

type="monotone"

dataKey="value"

/>

</LineChart>

</ResponsiveContainer>

</div>

);

};

export default DashboardChart;