package com.example.newsapp.util

sealed class Resource<T>(val data:T?=null,val message:String?=null){
    class Success<T>(data: T):Resource<T>(data)
    class Error<T>(message: String,data: T?=null):Resource<T>(data,message)
    class Loading<T>:Resource<T>()
}
//here we are using this to manage API response

//sealed classes is used to restricted or bounded class hierarchies
//A sealed class defines a set of subclasses within it.
// It is used when it is known in advance that a type will use one of the subclass types
//
//eg:-
//sealed class Fruit(val x : String)
//{
//    //Two subclasses of sealed class defined within
//    class Apple : Fruit("Apple")
//    class Mango : Fruit("Mango")
//}
// A subclass defined outside the sealed class
//class Pomegranate: Fruit("Pomegranate")
//
//fun main()
//{
//    //Objects of different subclasses created
//    val obj = Fruit.Apple()
//    val obj1 = Fruit.Mango()
//    val obj2 = Pomegranate()
//}
//in above example sealed class sets a hierarchy for apple and mango
//i.e they can only be initialised as Fruit.Apple()
//but pomegranate is not defined in hierarchy so it can be called directly