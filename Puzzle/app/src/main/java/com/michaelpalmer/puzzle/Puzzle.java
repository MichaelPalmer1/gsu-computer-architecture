package com.michaelpalmer.puzzle;

import android.util.SparseIntArray;

import java.util.ArrayList;

/**
 * To represent one puzzle instance based on a sample file
 */
public class Puzzle {

    private PuzzleState start_state, goal_state;

    Puzzle(String data, boolean is_file) throws UnsolvablePuzzleError {
        /*
        """
        Initialize the Puzzle

        :param str data: Can be string 0 - 8 or file path (if file path, set flag)
        :param bool is_file: Flag to set if passing in file
        :raises: UnsolvablePuzzleError
        """
        */
        SparseIntArray start_state, goal_state;

        if (is_file) {  // the data being passed in is a file to be parsed
            ArrayList<SparseIntArray> states = parse_file(data);
            start_state = states.get(0);
            goal_state = states.get(1);
        } else {  // the data being passed in is a string containing a start and goal state
            ArrayList<SparseIntArray> states = parse_full_data_string(data);
            start_state = states.get(0);
            goal_state = states.get(1);
        }

        // Initialize start and goal states
        this.start_state = new PuzzleState(start_state, this);
        this.goal_state = new PuzzleState(goal_state, this);

        // Calculate the aggregate heuristic cost using manhattan and linear conflict
        this.start_state.calc_aggregate_costs();

        // Check if the puzzle is not solvable
        if (!this.solvable()) {
            throw new UnsolvablePuzzleError("This Puzzle is not solvable.");
        }
    }

    private static ArrayList<SparseIntArray> parse_file(String filename) {
        /*
        Parse the file of the format:

        7 3 8
        0 2 5
        1 4 6

        0 1 2
        3 4 5
        6 7 8

        Where the first puzzle state is the initial state, and the second state is the goal state

        :param str filename: Path to the file
        :return: Parsed file
        :rtype: tuple
        */
        // Open the file
//        f = open(filename);

        // get the entire contents of the file
//        String data = f.read();
        String data = "";

        // parse both the start and initial state
        return parse_full_data_string(data);
    }

    private static SparseIntArray parse_data(String data) {
        /*
        Parse state data

        :param str data: string containing just one string state -> start state or goal state
        :return: State dictionary
        :rtype: dict
        */
        int pos = 0;
        SparseIntArray state_map = new SparseIntArray();

        data = data.replace(" ", "").replace("\n", "");
        for (int i = 0; i < data.length(); i++) {
            state_map.put(pos, Integer.parseInt(data.substring(i, i + 1)));
            pos += 1;
        }

        return state_map;
    }

    private static ArrayList<SparseIntArray> parse_full_data_string(String full_data_string) {
        /*
        Parse the full data string from a file and create appropriate start and goal maps

        :param str full_data_string: a string containing the start state and goal state,
                                        delimited with two newline chars
        :return: A tuple of the format (starting state dictionary, goal state dictionary)
        :rtype: tuple of dict
        */
        String[] tmp = full_data_string.split("\n\n");
        String start_data = tmp[0];
        String goal_data = tmp[1];

        SparseIntArray start_map = parse_data(start_data);
        SparseIntArray goal_map = parse_data(goal_data);

        ArrayList<SparseIntArray> result = new ArrayList<>();

        result.add(start_map);
        result.add(goal_map);

        return result;
    }

    /*
    def solve(self):
        """
        Solve it!

        :return: Solution
        :rtype: PuzzleState
        """
        open_states = PriorityQueue()
        closed_states = set()
        open_states_list = list()
        closed_states_list = list()

        # Add the start state to the frontier
        open_states.put((self.start_state.f, self.start_state.h, self.start_state.g, self.start_state))
        open_states_list.append(self.start_state.state)

        # Keep searching until the frontier is empty
        while open_states:

            # Pop from the priority queue and add the best state to the explored set
            current = open_states.get()[3]
            closed_states.add(current)

            # Update the actual states (dicts) that are in open/closed (this makes the search faster)
            open_states_list.remove(current.state)
            closed_states_list.append(current.state)

            # Have we reached the goal state?
            if current.validate_goal_state():
                return current

            # Loop through the possible actions from this state
            for child in current.actions():
                # If child is already in explored, skip to next child
                if child.state in closed_states_list:
                    continue

                # Set the child's parent
                child.parent = current

                # Add child to frontier if it's not in explored or frontier
                if child.state not in closed_states_list or child.state not in open_states_list:
                    open_states.put((child.f, child.h, child.g, child))
                    open_states_list.append(child.state)
    */

    boolean solvable() {
        /*
        Determine if this puzzle is solvable

        :return: True if the puzzle is solvable, otherwise False
        :rtype: bool
        */

        int start_inversions = Puzzle.inversions(start_state.state);
        int goal_inversions = Puzzle.inversions(goal_state.state);

        // the parity of inversions of the goal state and start state must be the same
        return (goal_inversions % 2 == 0) == (start_inversions % 2 == 0);
    }

    private static int inversions(SparseIntArray state) {
        /*
        There is a way to do this O(nlogn) instead of O(n^2) but can implement that later

        :param dict state: the mapping of positions to values
        :return: the number of inversions present in that state
        */
        int inversions = 0;
        PuzzleState[] values = list(state.values());
        values.remove(0);

        for (int i = 0; i < 7; i++) {
            for (int j = i + 1; j < 8; j++) {
                if (values[i] > values[j]) {
                    inversions += 1;
                }
            }
        }

        return inversions

    /*
    @staticmethod
    def solution_path(state):
        """
        Trace the path back from the specified state to the start state

        :param PuzzleState state: Solution state
        :return: Solution path
        :rtype: list of PuzzleState
        """
        path = [state]

        while state.parent:
            state = state.parent
            path.append(state)

        return path

    @staticmethod
    def print_path(state):
        """
        Print the path from the start state to the specified state.

        :param PuzzleState state: Puzzle state instance
        :return: Number of moves
        :rtype: int
        """
        solution_path = Puzzle.solution_path(state)

        moves = 0
        # reversed just returns an iterator, so no lengthy operations being done on the list
        for sol in reversed(solution_path):
            print('Move #%d' % moves)
            print(sol.print_state())
            moves += 1
        return moves - 1
    */

}