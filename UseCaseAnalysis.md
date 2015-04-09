# Use Case Diagram #


![https://cmpesweng2013group10.googlecode.com/files/use-case-diagram.png](https://cmpesweng2013group10.googlecode.com/files/use-case-diagram.png)


# Use Case Scenario #

**Use Case Name: Create A Task**

**Actors:** Member

**Goal:** Adding a task after a disaster

**Preconditions:**
  * Occurence of a disaster or an unnatural event
  * The information entered by the user should be valid

**Steps:**

  * An earthquake occurred with magnitude 7.1 in Zonguldak region.
  * There is a serious damage in some of buildings and there are many people who need a place to stay.
  * Ahmet who also lives in Zonguldak survived this earthquake and saw some of those people in his neighborhood.
  * He wanted to help them in an efficient way. He remembered that there is website which is very effective in organization of help in such situations.
  * Ahmet goes to this website and **requests a membership** and after validation of his membership he **logins** to the site.
  * Then he **creates a task** which is about to find 500 of tents for the homeless people.
  * System checks whether there is a **similar task** in the same place or not.
  * If there is, it warns Ahmet ‘There is also such a case, are you sure to continue?’.
  * If Ahmet is sure about adding this task, process will be completed.

**Postconditions:**

  * The task can be seen by public.
  * Any helper would use the task and get to know about the unnatural event

**Use Case Name: A member helps**

**Goal:** A member helps the lacker

**Preconditions:**
  * System presents the task
  * System lets the user to help and directs s/he to the lacker
  * System should indicate the percentage of task completion.

**Steps:**

  * After adding the task**for 500 of tents, a member saw it and wanted to**help those people.
  * S/he promise to send 400 tents.
  * After tents came, people give a **feedback** to the site, for instance, we took 400 of tents from this person.
  * And system will check that person whether s/he is a member of the site or not.
  * Then if s/he is a member, system will increase his or her points by using **point ranking algorithm**.
  * Also the number of tents arrived will be decreased from the total number of needed tents.
  * It goes to **task completion approval** and tested via **percentage of task completion algorithm**.
  * Finally, when the need of 500 tents is met, this task will be automatically **closed**.

**Postconditions:**

  * System should update the task completion bar.
  * System should close the task if the target need is met.
  * User's point should be updated.

**Use Case Name:** A guest helps

**Goal:** A guest helps the lacker

**Preconditions:**

  * User is not a member of the system.
  * System should be able to follow the amount of help.
  * System should be able to update the task completion bar.
**Steps:**
  * A guest saw the task and wanted to help those people.
  * S/he promise to send 100 tents.
  * After tents came, people give a **feedback** to the site, for instance, we took 100 of tents from this person.
  * And system will check that person whether s/he is a member of the site or not.
  * If not, do nothing about points.
  * The number of tents arrived will be decreased from the total number of needed tents.
  * It goes to **task completion approval** and tested via **percentage of task completion algorithm**.
  * Finally, when the need of 500 tents is met, this task will be automatically **closed**.

**Postconditions:**

  * System should be able to get the respond of the lacker
  * Updated bar can now be seen by others.