import sys
import tweepy

consumer_key        = 'LpACmv9AtbFEzas7CK6jpeYDF'
consumer_secret     = 'YQG1gCUfsoRWFao5Rh0H5dwxjdLP05rZgJ7RCxFsYBAQM5xx8z'
access_token        = '767334748247191552-EQ8SS8sI49EMV0jF02IdeQ1fhsxNGvL'
access_token_secret = 'orymGZ57CwdaER5pJxzAnfb82lvNNhbEYX41ALyLq1LYN'

auth = tweepy.OAuthHandler(consumer_key, consumer_secret)
auth.set_access_token(access_token, access_token_secret)

api = tweepy.API(auth, wait_on_rate_limit=True)

for user in tweepy.Cursor(api.followers, screen_name= sys.argv[1] ).items():
    print(user.screen_name + ", " user.name)
