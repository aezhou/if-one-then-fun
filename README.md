
if-one-then-fun
===============
# How to Run
Teams and their members are inputted to the program via a well-formatted JSON file. Fields are the name of the team and 
the members. 
```
{
    "name": "The Beatles",
    "members": [
       "paul@mccartney.org",
        "john@lennon.com",
        "george@harrison.org",
        "ringo@starr.org"
        ]
    },
    {
        "name": "The Quarrymen",
        "members": [
            "paul@mccartney.org",
            "john@lennon.org",
            "stu@sutcliffe.org"
    ]
},
{
    "name": "Wings",
    "members": [
        "paul@mccartney.org",
        "linda@mccartney.org"
    ]
},
{
    "name": "Plastic Ono Band",
    "members": [
        "john@lennon.org",
        "yoko@ono.org"
    ]
},
{
    "name": "Traveling Wilburys",
    "members": [
        "george@harrison.org",
        "tom@petty.org",
        "roy@orbison.org"
    ]
}
```
Run the command below in the terminal to start the program:

`java -jar ./out/artifacts/if_one_then_fun_jar/if-one-then-fun.jar sample_input.json`
# The strategy you employed to generate 1+1 pairs
To create the 1+1 pairs, the following rules were followed:
- A person should first be matched with his/her teammates. Once all teammates (across all teams) have been matched with,
 random matchings with others who are in the same situation are allowed.
- A person can only be in at most one 1+1 pair per week.
- Every person in the organization will participate in a 1+1 pair each week, unless there is an odd number of employees
  in the organization. In this situation, one person each week will not have a pairing (who this will be is determined 
  by the matching algorithm).
- The same two teammates should not be paired together in consecutive weeks. However, if they are being randomly paired 
  with others who have been matched with all possible teammates already, consecutive pairings are possible (but not 
  likely).
- Teammates should be paired with equal frequency: if **A** and **B** are paired together this week, **A** shouldn't be 
  paired with **B** again until **A** has paired with every other person on the team. This is accomplished by keeping
  track of which teammates **A** has and has not been matched with yet. 

The matching protocol is as follows:
- Matchings are assigned to people who have the least number of potential matches with teammates to the greatest number 
  of potential matches with teammates.
- Each person randomly picks someone to match with from their possible matches. If one of their teammates (that they 
  haven't matched with yet) is already in a matching for this round, the teammate is not considered to be a possible 
  match. A business match is denoted with "for team bonding" in the list of matchings.
- Once a person has had matchings with all teammates, across all teams, they enter the random "pool" of employees. From 
  this pool, matchings are assigned randomly, and designated by the text "for fun :)" in the list of matchings.
- If a person has potential teammates, but they have all already been assigned matchings for this round, this person 
  will enter the random pool of employees as well and receive a matching that is just for fun.
- If there is an odd number of people, one person per week will not receive a matching. This person is randomly chosen
  from the pool of employees, and is listed as "unmatched" in the list of matchings.
- This algorithm wants every employee to be matched with all of its teammates before allowing any employee to start the 
  process over and have a second matching with its teammates. A threshold in the code is programmatically set such that 
  once X% of the company has been completely matched, the matching process starts over and allows employees to have a 
  second matching with their teammates. (An employee is considered to be completely matched if he/she has had matchings
  with all of his/her teammates, across all teams.)

# Testing methodology
Testing was done manually due to time constraints. Using the debugging tools, I could see the state of the various sets 
at each step of the process, and manually calucated matchings and compared these to what was being outputted. I varied 
the input to ensure that there were people with only one possible matching, no possible matchings, and many cross-team 
possible matchings.

However, with time permitting, many different unit tests could have been written. All substantial logic was extracted 
into various methods for ease of testing. In the Matching class, it would be easy to test both types of matching 
(random matching and matching with a teammate whom you have not matched with before) - testing with various sizes of 
the "matched" set and random pool (if applicable). It is also easy to test the other methods that require inputs. 
Updating the matching table is easily verifyable if done successfully. When getting possible matches, we can vary the 
overlap between a person's potential matches and the people who have been assigned matchings for this round.

Another method that needs testing is in Organization - the assigning teams method. In this case, it is important to 
make sure that each person has the correct teammates in their potential teammates (and does not include themselves).

Finally, we need to make sure that we are parsing the JSON file correctly. We only want one Person instance per unique
email address. Additionally, we want to make sure that the teams are correctly created.
