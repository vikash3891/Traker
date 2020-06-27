//package com.sportsfanatiq.cricket.network


/**
 * Created by bms on 19/2/18 added in com.sportsfanatiq.cricket.network
 */
class ApiConstants {

    companion object {
        //      private const val BASE_URL = "http://13.126.178.40:93/api/"
//        private const val BASE_URL = "http://apitesting.bookmysports.com/api/"
        private const val BASE_URL = "http://api-dev.tripoturf.com/api/"

        /*** For Development Images  */
        const val IMAGE_URL = "https://s3-ap-southeast-1.amazonaws.com/bms-dev/"

        const val GET_PARTICIPATING_TEAMS = BASE_URL + "team/TeamsUsersParticipatesIn"
        const val GET_ALL_TEAMS = BASE_URL + "Team/GetAllTeams"
        const val GET_MY_TEAMS = BASE_URL + "team/GetUserTeams"
        const val GET_MY_TEAMS_TOURNAMENT = BASE_URL + "Tournament/GetTeamForTournament"
        const val SIGN_UP = BASE_URL + "Account/SignUp"
        const val SIGN_IN = BASE_URL + "Account/Login"
        const val SEARCH_GROUND = BASE_URL + "Ground/SearchGround"
        const val SEARCH_TOURNAMENT_GROUND = BASE_URL + "Ground/SearchGroundByCity"
        const val USER_MATCHES = BASE_URL + "Match/GetAssignedMatches"
        const val RESEND_OTP = BASE_URL + "Account/ResendOTP"
        const val VERIFY_OTP = BASE_URL + "Account/VerifyOTP"
        const val CREATE_TEAM = BASE_URL + "Team/CreateTeam"
        const val ASSIGNED_MATCHES = "Match/GetUserMatches"
        const val DELETE_TEAM = BASE_URL + "Team/deleteTeam"
        const val UPLOAD_IMAGE = BASE_URL + "upload/UploadImage"
        const val UPLOAD_TEAM_LOGO = BASE_URL + "Upload/UploadTeamLogo"
        const val UPLOAD_TEAM_BANNER = BASE_URL + "Upload/UploadTeamBanner"
        const val UPLOAD_PROFILE_PHOTO = BASE_URL + "Upload/UploadUserProfilePhoto"
        const val UPLOAD_PROFILE_BANNER = BASE_URL + "Upload/UploadUserProfileBanner"
        const val GET_TEAM_DETAIL = BASE_URL + "Team/GetTeamDetail?TeamId="
        const val COUNTRY = BASE_URL + "Country/GetCountryDetail"
        const val CREATE_MATCH = BASE_URL + "Match/CreateMatch"
        const val TOSS_DETAIL = BASE_URL + "Match/TossDetail"
        const val GET_MATCH_DETAIL = BASE_URL + "Match/GetMatchDetails?matchID="
        const val GET_TOURNAMENT_TYPE = BASE_URL + "Tournament/GetTournamentType"
        const val GET_TOURNAMENT_OVERVIEW = BASE_URL + "tournament/gettournamentoverview?tournamentid="
        const val GET_GROUP = BASE_URL + "pool/getpoolsdetail?tournamentid="
        const val ADD_GROUP = BASE_URL + "pool/AddUpdatePool"
        const val GET_TOURNAMENT_TEAM = BASE_URL + "Tournament/GetTournamentTeams"
        const val GET_POOL_TEAM = BASE_URL + "pool/GetPoolTeam?tournamentid="
        const val ADD_TEAM_IN_TOURNAMENT = BASE_URL + "Tournament/AddTeamToTournament"
        const val SEARCH_TEAM_INTOURNAMENT = BASE_URL + "Team/SearchTeams"

        var COUNTRY_FILE: String = "CountryFile"
        var COUNTRY_OBJECT: String = "country"

        const val MATCH_TEAM_DETAIL = BASE_URL + "match/GetMatchTeamDetails?matchid="

        const val USER_EXIST = BASE_URL + "account/IsUserExist"
        const val TEAM_EXIST = BASE_URL + "team/IsTeamExist"

        const val ADD_MATCH_PLAYERS = BASE_URL + "match/AddMatchPlayer"
        const val TEAM_PLAYER_LIST = BASE_URL + "Team/GetTeamPlayers?TeamId="
        const val TEAM_PLAYER_LIST_WHILE_UPDATE = BASE_URL + "match/GetMatchTeamPlayingPlayers"

        const val ADD_TEAM_PLAYER_LIST = BASE_URL + "player/AddPlayerInTeam"
        const val GET_PLAYER_DETAIL = BASE_URL + "Player/GetPlayerDetail"
        const val GET_SEARCH_PLAYER = BASE_URL + "player_item/SearchPlayer"

        const val GET_ALL_MATCHES = BASE_URL + "match/GetAllMatches"
        const val GET_ASSIGNED_MATCHES = BASE_URL + "match/GetAssignedMatches"
        const val GET_MY_MATCHES = BASE_URL + "match/GetMyMatches"
        const val GET_PARTICIPATING_MATCHES = BASE_URL + "match/GetPlayersParticipatingMatches"
        const val UPDATE_PLAYER_ROLE = BASE_URL + "match/UpdateMatchPlayerRole"
        const val MATCH_RESULT = BASE_URL + "Scoring/MatchResult"
        const val BALL_OUT = BASE_URL + "scoring/BowlOutStatus"
        const val UPLOAD_MATCH_SCORE = BASE_URL + "Scoring/Scoring"
        const val GET_SCORECARD = BASE_URL + "Scorecard/MatchInningDetails"
        const val CONTACT_US = BASE_URL + "ContactUs/ContactUs"
        const val CREATE_TOURNAMENT = BASE_URL + "Tournament/CreateTournament"
        const val SOCIAL_MEDIA_SIGNIN = BASE_URL + "Account/SignUpWithSocialMedia"
        const val CHANGE_PASSWORD = BASE_URL + "Account/ChangePassword"


        /*******************************Viewing Api's****************************************/

        const val GET_PLAYER_OVERVIEW = BASE_URL + "PlayerCricketProfile/GetPlayerOverView"
        const val GET_PLAYER_BASIC_DETAIL = BASE_URL + "PlayerCricketProfile/GetPlayerBasicDetail"
        const val GET_PLAYER_MATCHTYPE = BASE_URL + "PlayerCricketProfile/PlayerCareerMatchTypeWise"
        const val GET_PLAYER_PROFILE_YEARWISE = BASE_URL + "PlayerCricketProfile/PlayerCricketCareerYearWise"
        const val GET_PLAYER_TOURNAMENTWISE = BASE_URL + "PlayerCricketProfile/PlayerCricketCareerTournamentWise"
        const val GET_PLAYER_MATCHWISE = BASE_URL + "PlayerCricketProfile/PlayerCricketCareerMatchWise"
        const val GET_MATCH_INFO = BASE_URL + "Match/GetMatchInfo?matchId="
        const val GET_SQUAD_DETAIL = BASE_URL + "Match/GetMatchTeamsDetail?matchID="
        const val GET_TOURNAMENT_MATCHES = BASE_URL + "tournament/GetTournamentMatches"
        const val GET_ALL_TOURNAMENTS_LIST = BASE_URL + "tournament/GetAllTournament"
        const val GET_MY_TOURNAMENT_LIST = BASE_URL + "tournament/GetMyTournament"
        const val GET_SCHEDULER_TYPE = BASE_URL + "Match/GetSchedulerType"
    }
}