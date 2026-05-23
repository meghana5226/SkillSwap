const ProfileHeader = ({ user }) => {

return(

<div className="profile-header">

<div className="profile-left">

<img
src={
user?.profilePicture ||
"https://ui-avatars.com/api/?name=User"
}
alt="profile"
/>

<div>

<h1>

{user?.name}

</h1>

<p>

{user?.headline || "SkillSwap User"}

</p>

<p>

📍 {user?.location}

</p>

</div>

</div>


<div className="profile-rating">

⭐ {user?.rating || "0.0"}

</div>

</div>

);

};

export default ProfileHeader;