# Math Game Client-Server System

## Project Description

The implemented game is a MathGame where, in each round, the clients receive a calculation (Sum, Subtraction, Multiplication, or Division) to solve. Whoever replies first gets points. The game is playable 2 by 2. Unfortunately, we didn't manage to support more than 2 users per game.

## Technologies Used

- Java
    - TCP Sockets
    -   Java Threads
    - Reentrant locks


## Prerequisites

- Java Development Kit (JDK) 21 or higher installed on your computer
- A terminal or command line interface to run the commands

## How to Run the Project

### Steps

1. **Clone the Repository or Download the Files**

   ```sh
   git clone git clone https://github.com/SofiaValadares/Java-TCP_sockets_and_multi-threadin
   

2. **Compile the Java Code**

Open a terminal in the project directory and compile the Java files using the following command:

`javac src/pt/fe/up/cpd/t4g11/main/controller/*.java src/pt/fe/up/cpd/t4g11/main/model/*.java`

3. **Run the Server**

In the terminal, navigate to the src directory and start the server with a specified port number (e.g., 8000):

`java src.pt.fe.up.cpd.t4g11.main.controller.Server 8000`

4. Run the Client(s)

Open another terminal (you can open multiple terminals for multiple clients) and navigate to the src directory. Run the client with the server's hostname (usually localhost) and the same port number used for the server (e.g., 8000):


`java src.pt.fe.up.cpd.t4g11.main.controller.Client localhost 8000`

Repeat this step to start multiple games.

The game will automatically start once 2 users are connected. It will first ask for a nickname and then password, after replying, the client will be in queue so start the game with another. After the game starts, there will a line in below part of the screen that will open.

5. **Enjoy the game!** :)

In case of doubts you can reach out to the team :)

# Turma 13 - Grupo 11
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/gabrieltmjr">
        <img src="https://avatars.githubusercontent.com/u/73040950?v=4" width="100px;" alt="Foto Gabriel"/><br>
        <sub>
          <b>Gabriel Machado Jr</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/guiga-sa">
        <img src="https://avatars.githubusercontent.com/u/123979639?v=4" width="100px;" alt="Foto Guilherme"/><br>
        <sub>
          <b>Guilherme Araujo</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/SofiaValadares">
        <img src="https://avatars.githubusercontent.com/u/113111708?v=4" width="100px;" alt="Foto Sofia Valadares"/><br>
        <sub>
          <b>Sofia Valadares</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
<br>
