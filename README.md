# SpaceShare

## Work Log
* Minn - 2 hours (2023-06-07) [Commit](https://github.com/jimmyfong0/SpaceShare/commit/f109ce53629aae603deb0c328f073f2243ca8684)
  + Setup navigation graph, activities, and fragments
  + Enable Firebase user login
* Minn - 1.5 hours (2023-06-11) [Commit](https://github.com/jimmyfong0/SpaceShare/commit/37615e8fb5807da3e3a38e14798c2ed30593bea4)
  + Added BottomNavigationView to navigate between core pages
  + Styled aforementioned view
* Minn - 4.5 hours (2023-06-14) [Commit](https://github.com/jimmyfong0/SpaceShare/commit/143493acaf942af6fc6fb7b51004cbc120dc3c34)
  + Added and configured Hilt dependency injection
  + Made ViewPager2 adapter to enable user to swipe between space images
  + Fetch listing functionality now works on dummy data
  + TODO:
    * Merge Chang's publish listing functionality into ListingRepository
    * Add logout button
    * Configure User model to be dependency injected so it is accessible from any Fragment
    * Complete fetch listing functionality to fetch from dynamic user data instead of hard-coded ID
    * It's 1.32AM and I am tired
