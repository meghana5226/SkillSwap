const ProfileCard = ({
    user
}) => {

return (

<div className="profile-card">

<h2>
{user.name}
</h2>

<p>
{user.headline}
</p>

<p>
📍 {user.location}
</p>

<p>
{user.bio}
</p>

</div>

);

};

export default ProfileCard; 