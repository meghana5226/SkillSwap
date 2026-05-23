import { useEffect, useState } from "react";

import ProfileHeader from "../../components/profile/ProfileHeader";
import UserSkillList from "../../components/profile/UserSkillList";

import { getProfile } from "../../api/profileApi";

const ProfilePage = () => {

    const [user,setUser] =
    useState(null);

    useEffect(()=>{

        loadProfile();

    },[]);


    const loadProfile = async()=>{

        try{

            const data =
            await getProfile();

            setUser(data);

        }

        catch(error){

            console.log(
                "Profile load failed",
                error
            );

        }

    };

    return(

        <div className="profile-page">

            <ProfileHeader
            user={user}
            />

            <div
            className="profile-sections"
            >

                <div
                className="profile-card"
                >

                    <h2>

                        About

                    </h2>

                    <p>

                        {user?.bio || "No bio added"}

                    </p>

                </div>

                <UserSkillList
                skills={
                    user?.skills || []
                }
                />

            </div>

        </div>

    );

};

export default ProfilePage;