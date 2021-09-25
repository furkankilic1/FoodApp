# FoodApp
 
 ### An android mobile application that;
* Users encounter the homepage where popular dishes and categories of the regions are listed.
* When the user clicks on the region or category, the user reaches the menu of the relevant region or category. 
* Then, the user who wants to go to the details of the food opens the detail page by clicking on the relevant dish.

 ### 1) Home Page
 * Regions are taken with the following api request and shown as the first item of the screen.
   https://www.themealdb.com/api/json/v1/1/list.php?a=list
 * The categories are taken with the following api request and displayed as the second item of the screen.
   https://www.themealdb.com/api/json/v1/1/categories.php
 * The first item can be scrolled horizontally, the second item can be scrolled vertically.
 
![home1](https://user-images.githubusercontent.com/58864953/134777142-d9bd26af-e040-4725-87ca-570ddbbe006e.png) ![t1](https://user-images.githubusercontent.com/58864953/134777563-db507874-a892-483c-94f3-eb4c26e4cded.png)

### 2) Food List
* The user can reach this screen in two different ways, by clicking on the region or category.

  ![as2](https://user-images.githubusercontent.com/58864953/134777597-03aba631-b666-4075-be2b-3c0ddb876f7d.png) ![ads](https://user-images.githubusercontent.com/58864953/134777627-cc0fc1cb-1a53-4350-880c-694ef79e5ae1.png)
  
* The users can favorite the food they want on the menu screen, the favorite food is indicated to the user with an icon. This added favorite can be removed by clicking the icon again. Favorite food will be stored in local db using SqLite.

![f1](https://user-images.githubusercontent.com/58864953/134777504-9aebad79-e298-4923-be05-27ceff34c6e9.png) ![f2](https://user-images.githubusercontent.com/58864953/134777505-daa71e88-5a4a-41d0-a065-572ca2441d4b.png)

* In case the user comes from the region, the popular menu is drawn with the request below.
  https://www.themealdb.com/api/json/v1/1/filter.php?a=Canadian  
  (In the API request, the query string value has been added to Canadian for example purposes.)
  
  ![a1](https://user-images.githubusercontent.com/58864953/134777280-f92f3ed6-b259-4371-951a-b39b94a0c62b.png) 
  
* In case the user comes through the category, the popular menu is drawn with the request below.
  https://www.themealdb.com/api/json/v1/1/filter.php?c=Dessert
  (In the API request, the query string value has been added to Dessert for example purposes.)
  
  ![a2](https://user-images.githubusercontent.com/58864953/134777312-bf9b3ca2-5ed9-4e70-b6df-173af69fc8fb.png)
  
### 3) Meal Detail
* The user reaches this screen by clicking on the food list.
  https://www.themealdb.com/api/json/v1/1/lookup.php?i=52767
  (In the API request, the query string value has been added to 52767 for example purposes.)
  
  ![aa3](https://user-images.githubusercontent.com/58864953/134777371-adcfa01b-dba0-4356-b5a8-b747a10538f0.png) ![asdads](https://user-images.githubusercontent.com/58864953/134777655-ee8b285a-33fe-4d5b-b1e6-4c36e3987226.png)
  
