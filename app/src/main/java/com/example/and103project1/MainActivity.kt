package com.example.and103project1

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// author: calren

object FourLetterWordList {
    // List of most common 4 letter words from: https://7esl.com/4-letter-words/
    val fourLetterWords =
        "Area,Army,Baby,Back,Ball,Band,Bank,Base,Bill,Body,Book,Call,Card,Care,Case,Cash,City,Club,Cost,Date,Deal,Door,Duty,East,Edge,Face,Fact,Farm,Fear,File,Film,Fire,Firm,Fish,Food,Foot,Form,Fund,Game,Girl,Goal,Gold,Hair,Half,Hall,Hand,Head,Help,Hill,Home,Hope,Hour,Idea,Jack,John,Kind,King,Lack,Lady,Land,Life,Line,List,Look,Lord,Loss,Love,Mark,Mary,Mind,Miss,Move,Name,Need,News,Note,Page,Pain,Pair,Park,Part,Past,Path,Paul,Plan,Play,Post,Race,Rain,Rate,Rest,Rise,Risk,Road,Rock,Role,Room,Rule,Sale,Seat,Shop,Show,Side,Sign,Site,Size,Skin,Sort,Star,Step,Task,Team,Term,Test,Text,Time,Tour,Town,Tree,Turn,Type,Unit,User,View,Wall,Week,West,Wife,Will,Wind,Wine,Wood,Word,Work,Year,Bear,Beat,Blow,Burn,Call,Care,Cast,Come,Cook,Cope,Cost,Dare,Deal,Deny,Draw,Drop,Earn,Face,Fail,Fall,Fear,Feel,Fill,Find,Form,Gain,Give,Grow,Hang,Hate,Have,Head,Hear,Help,Hide,Hold,Hope,Hurt,Join,Jump,Keep,Kill,Know,Land,Last,Lead,Lend,Lift,Like,Link,Live,Look,Lose,Love,Make,Mark,Meet,Mind,Miss,Move,Must,Name,Need,Note,Open,Pass,Pick,Plan,Play,Pray,Pull,Push,Read,Rely,Rest,Ride,Ring,Rise,Risk,Roll,Rule,Save,Seek,Seem,Sell,Send,Shed,Show,Shut,Sign,Sing,Slip,Sort,Stay,Step,Stop,Suit,Take,Talk,Tell,Tend,Test,Turn,Vary,View,Vote,Wait,Wake,Walk,Want,Warn,Wash,Wear,Will,Wish,Work,Able,Back,Bare,Bass,Blue,Bold,Busy,Calm,Cold,Cool,Damp,Dark,Dead,Deaf,Dear,Deep,Dual,Dull,Dumb,Easy,Evil,Fair,Fast,Fine,Firm,Flat,Fond,Foul,Free,Full,Glad,Good,Grey,Grim,Half,Hard,Head,High,Holy,Huge,Just,Keen,Kind,Last,Late,Lazy,Like,Live,Lone,Long,Loud,Main,Male,Mass,Mean,Mere,Mild,Nazi,Near,Neat,Next,Nice,Okay,Only,Open,Oral,Pale,Past,Pink,Poor,Pure,Rare,Real,Rear,Rich,Rude,Safe,Same,Sick,Slim,Slow,Soft,Sole,Sore,Sure,Tall,Then,Thin,Tidy,Tiny,Tory,Ugly,Vain,Vast,Very,Vice,Warm,Wary,Weak,Wide,Wild,Wise,Zero,Ably,Afar,Anew,Away,Back,Dead,Deep,Down,Duly,Easy,Else,Even,Ever,Fair,Fast,Flat,Full,Good,Half,Hard,Here,High,Home,Idly,Just,Late,Like,Live,Long,Loud,Much,Near,Nice,Okay,Once,Only,Over,Part,Past,Real,Slow,Solo,Soon,Sure,That,Then,This,Thus,Very,When,Wide"

    // Returns a list of four letter words as a list
    fun getAllFourLetterWords(): List<String> {
        return fourLetterWords.split(",")
    }

    // Returns a random four letter word from the list in all caps
    fun getRandomFourLetterWord(): String {
        val allWords = getAllFourLetterWords()
        val randomNumber = (0..allWords.size).shuffled().last()
        return allWords[randomNumber].uppercase()
    }
}

private fun checkGuess(guess: String, wordToGuess: String): SpannableStringBuilder {
    val result = SpannableStringBuilder()
    for (i in 0..3) {
        when {
            guess[i] == wordToGuess[i] -> {
                val greenSpan = SpannableString(guess[i].toString()).apply {
                    setSpan(ForegroundColorSpan(Color.GREEN), 0, length, 0)
                }
                result.append(greenSpan)
            }
            guess[i] in wordToGuess -> {
                val yellowSpan = SpannableString(guess[i].toString()).apply {
                    setSpan(ForegroundColorSpan(Color.RED), 0, length, 0)
                }
                result.append(yellowSpan)
            }
            else -> {
                val whiteSpan = SpannableString(guess[i].toString()).apply {
                    setSpan(ForegroundColorSpan(Color.BLACK), 0, length, 0)
                }
                result.append(whiteSpan)
            }
        }
    }
    return result
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var solution = FourLetterWordList.getRandomFourLetterWord()

        val checkGuessButton = findViewById<Button>(R.id.checkGuess)

        val guessEntryBox = findViewById<EditText>(R.id.guessEntry)

        val guessesText = findViewById<TextView>(R.id.previousGuesses)

        val solutionText = findViewById<TextView>(R.id.solution)

        val resetButton = findViewById<Button>(R.id.reset)

        val streak = findViewById<TextView>(R.id.streak)

        var solved = false

        var previousGuessesText = SpannableStringBuilder("Previous Guesses: \n")

        var leftGuesses = 3

        var streakRecord = 0

        fun String.isOnlyLetters(): Boolean = this.all { it.isLetter() }

        resetButton.setOnClickListener{
            leftGuesses = 3
            solution = FourLetterWordList.getRandomFourLetterWord()
            previousGuessesText = SpannableStringBuilder("Previous Guesses: \n")
            guessesText.text = previousGuessesText
            solutionText.text = ""
            solved = false
            Toast.makeText(it.context, solution, Toast.LENGTH_SHORT).show()
        }

        checkGuessButton.setOnClickListener {
            val guess = guessEntryBox.text.toString().uppercase()
            // Check if there are guesses left and if the guess is 4 letter
            if (!solved) {
                if (!guess.isOnlyLetters()){
                    Toast.makeText(it.context, "Only include letters", Toast.LENGTH_SHORT).show()
                    guessEntryBox.setText("")
                }
                else if (guess.length < 4) {
                    Toast.makeText(it.context, "Enter a 4 letter word", Toast.LENGTH_SHORT).show()
                    guessEntryBox.setText("")
                }
                else if (leftGuesses > 0) {
                    val guessStatus = checkGuess(guess, solution)

                    previousGuessesText.append(guessStatus).append("\n")

                    if (guess == solution){
                        solutionText.text = "⭐You solved it!⭐"
                        solved = true
                        streakRecord ++

                        streak.text = "Streak⚡: ".plus(streakRecord.toString())
                    }

                    guessesText.text = previousGuessesText

                    guessEntryBox.setText("")

                    leftGuesses--

                    if (leftGuesses == 0) {
                        streakRecord = 0
                        streak.text = "Streak⚡: 0"
                        solutionText.text = "The solution was: ".plus(solution)
                    }
                }
                else {
                    Toast.makeText(it.context, "Out of guesses", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(it.context, "You already Solved it!!!", Toast.LENGTH_SHORT).show()
            }

        }
    }


}