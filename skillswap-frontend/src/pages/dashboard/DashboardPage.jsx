import { useEffect,useState } from "react";

import Sidebar
from "../../components/navigation/Sidebar";

import Topbar
from "../../components/dashboard/Topbar";

import StatCard
from "../../components/dashboard/StatCard";

import DashboardChart
from "../../components/dashboard/DashboardChart";

import ActivityFeed
from "../../components/dashboard/ActivityFeed";

import { getDashboardStats }
from "../../api/dashboardApi";

const DashboardPage=()=>{

const [stats,setStats]=
useState(null);

useEffect(()=>{

loadDashboard();

},[]);

const loadDashboard=
async()=>{

try{

const data=
await getDashboardStats();

setStats(data);

}

catch{

console.log(
"Dashboard load failed"
);

}

};

const chartData=[

{
month:"Jan",
value:4
},

{
month:"Feb",
value:7
},

{
month:"Mar",
value:5
},

{
month:"Apr",
value:10
},

{
month:"May",
value:8
}

];

return(

<div
className="dashboard-wrapper"
>

<Sidebar/>

<div
className="dashboard-main"
>

<Topbar/>

<div
className="stats-grid"
>

<StatCard
title="Skills"
value={
stats?.totalSkills || 0
}
icon="📘"
/>

<StatCard
title="Requests"
value={
stats?.receivedRequests || 0
}
icon="📩"
/>

<StatCard
title="Completed"
value={
stats?.completedRequests || 0
}
icon="✅"
/>

<StatCard
title="Accepted"
value={
stats?.acceptedRequests || 0
}
icon="⭐"
/>

</div>


<div
className="dashboard-lower"
>

<DashboardChart
data={chartData}
/>

<ActivityFeed/>

</div>

</div>

</div>

);

};

export default DashboardPage;