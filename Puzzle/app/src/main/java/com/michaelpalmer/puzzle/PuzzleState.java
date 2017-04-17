package com.michaelpalmer.puzzle;

import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class PuzzleState {

    // Map tile positions to coordinates for use by Manhattan Distance
    static HashMap<Integer, ArrayList<Integer>> coord_map =  new HashMap<>();

    static {
        coord_map.put(0, (ArrayList<Integer>) Arrays.asList(0, 0));
        coord_map.put(1, (ArrayList<Integer>) Arrays.asList(1, 0));
        coord_map.put(2, (ArrayList<Integer>) Arrays.asList(2, 0));
        coord_map.put(3, (ArrayList<Integer>) Arrays.asList(0, -1));
        coord_map.put(4, (ArrayList<Integer>) Arrays.asList(1, -1));
        coord_map.put(5, (ArrayList<Integer>) Arrays.asList(2, -1));
        coord_map.put(6, (ArrayList<Integer>) Arrays.asList(0, -2));
        coord_map.put(7, (ArrayList<Integer>) Arrays.asList(1, -2));
        coord_map.put(8, (ArrayList<Integer>) Arrays.asList(2, -2));
    }

    private int g, h, f;
    private PuzzleState parent;
    public SparseIntArray state;
    private Puzzle puzzle;

    PuzzleState(SparseIntArray state, Puzzle puzzle) {
        /*
        Initialize a puzzle state

        :param dict state: Puzzle state dictionary
        :param Puzzle puzzle: Puzzle instance
        */

        // Initialize g, h, and f cost values
        this.g = 0;
        this.h = 0;
        this.f = 0;

        // Initialize parent to a null value
        this.parent = null;

        // Pass in the state dict
        this.state = state;

        // pass a reference the puzzle's goal state in order for this instance to check for a match
        this.puzzle = puzzle;
    }

    public String toString() {
        /*
        Return string representation of the puzzle state

        :return: Puzzle State as a string
        :rtype: str
        */
        return this.print_state();
    }

    String __repr__() {
        /*
        Generate puzzle state representation

        :return: Puzzle State representation
        :rtype: str
        */
        return this.print_state();
    }


    static ArrayList<Integer> valid_movement_positions(int position) {
        /*
        Given the position of the empty square in the puzzle, determine the squares that can make a valid move.

        :param int position: Position
        :return: Valid movement positions
        :rtype: tuple of int
        */
        if (position == 0) {
            return (ArrayList<Integer>) Arrays.asList(1, 3);

        } else if (position == 1) {
            return (ArrayList<Integer>) Arrays.asList(0, 2, 4);

        } else if (position == 2) {
            return (ArrayList<Integer>) Arrays.asList(1, 5);

        } else if (position == 3) {
            return (ArrayList<Integer>) Arrays.asList(0, 4, 6);

        } else if (position == 4) {
            return (ArrayList<Integer>) Arrays.asList(1, 3, 5, 7);

        } else if (position == 5) {
            return (ArrayList<Integer>) Arrays.asList(2, 4, 8);

        } else if (position == 6) {
            return (ArrayList<Integer>) Arrays.asList(3, 7);

        } else if (position == 7) {
            return (ArrayList<Integer>) Arrays.asList(6, 4, 8);

        } else if (position == 8) {
            return (ArrayList<Integer>) Arrays.asList(5, 7);
        }
        return new ArrayList<>();
    }

    boolean validate_goal_state() {
        /*
        Check if the goal state has been reached

        :return: True if all nodes are in their proper place, False otherwise
        :rtype: bool
        */
        return this.state == this.puzzle.goal_state.state;
    }

    void move_node(int moving_node) {
        /*
        Switches a real node with the node holding the val 0

        :param int moving_node: The node that is being moved into the "empty" space, which is just a node with val 0
        */
        // create dummy vars to hold the positions of each node while we switch
        int moving_pos = this.node_position(moving_node);
        int empty_pos = this.node_position(0);

        // switch the nodes in the puzzle states dict
        this.state.put(empty_pos, moving_node);
        this.state.put(moving_pos, 0);

        this.calc_aggregate_costs();
    }

    String print_state() {
        /*
        Print the current state of the puzzle

        :return: a string representing the puzzles state
        :rtype: str
        */
        int cnt = 1;
        String puzzle_state = "";
        for (int i = 0; i < this.state.size(); i++) {
            int node = this.state.get(i);
            if (node == 0) {
                node = -1;
            }
            if (cnt % 3 == 0) {
                puzzle_state += String.format("%s\n", node == -1 ? " " : node);
            } else {
                puzzle_state += String.format("%s ", node == -1 ? " " : node);
            }

            cnt += 1;
        }

        return puzzle_state;
    }

    int node_position(int node) {
        /*
        Returns the given nodes position in the current state

        :param int node: the node to search for
        :return: the position of the given node in the state
        :rtype: int
        */

        for (int i = 0; i < this.state.size(); i++) {
            if (this.state.get(i) == node) {
                return i;
            }
        }
        return -1;
    }


    ArrayList<PuzzleState> actions() {
        /*
        Generate the possible actions (PuzzleStates) that can be achieved from the current state

        :return: List of actions
        :rtype: list of PuzzleState
        */
        int node_pos = this.node_position(0);
        ArrayList<Integer> valid_movement_positions = PuzzleState.valid_movement_positions(node_pos);
        ArrayList<PuzzleState> actions = new ArrayList<>();
        int g_cost = this.g + 1;

        for (int i = 0; i < this.state.size(); i++) {
            int child = this.state.get(i);
            if (valid_movement_positions.contains(i)){
                // Make a copy
                SparseIntArray copied_state = this.state.clone();
                PuzzleState new_state = new PuzzleState(copied_state, this.puzzle);
                new_state.g = g_cost;

                // Move the node in the new copy
                new_state.move_node(child);

                // Add to actions
                actions.add(new_state);
            }
        }

        return actions;
    }

    int calc_manhattan(int pos, int node) {
        /*
        Calculate the manhattan distance.

        Can calculate the both the g and h costs with the associated flags
        :param int pos: Current node position
        :param int node: Target node
        :return: Manhattan distance
        :rtype: int
        */
        int end = this.puzzle.goal_state.node_position(node);
        int[] current = this.coord_map[pos];
        int current_x, current[0], current_y = current[1];
        goal_x, goal_y = coord_map[end];

        dst = Math.abs(current_x - goal_x) + Math.abs(current_y - goal_y);

        return dst;
    }

    int calc_linear_conflict() {
        /*
        Calculate the linear conflict of this state

        Two tiles tj and tk are in a linear conflict if:
         - tj and tk are in the same line
         - goal positions of tj and tk are both in that line
         - tj is to the right of tk
         - goal position of tj is to the left of the goal position of tk

        :return: Linear conflict
        :rtype: int
        */
        int linear_vertical_conflict = 0;
        int linear_horizontal_conflict = 0;

        int[][] rows = new int[][]{
                {this.state.get(0), this.state.get(1), this.state.get(2)},
                {this.state.get(3), this.state.get(4), this.state.get(5)},
                {this.state.get(6), this.state.get(7), this.state.get(8)}
        };

        // Calculate vertical conflicts
        for (int row = 0; row < rows.length; row++) {
            int[] row_list = rows[row];
            int maximum = -1;
            for (int value : row_list) {
                if (value != 0 && (value - 1) / 3 == row) {
                    if (value > maximum) {
                        maximum = value;
                    } else {
                        linear_vertical_conflict += 2;
                    }
                }
            }
        }

        int[][] cols = new int[][]{
                {this.state.get(0), this.state.get(3), this.state.get(6)},
                {this.state.get(1), this.state.get(4), this.state.get(7)},
                {this.state.get(2), this.state.get(5), this.state.get(8)}
        };

        // Calculate horizontal conflicts
        for (int col = 0; col < cols.length; col++) {
            int maximum = -1;
            int[] col_list = cols[col];
            for (int value : col_list) {
                if (value != 0 && value % 3 == col + 1) {
                    if (value > maximum) {
                        maximum = value;
                    } else {
                        linear_horizontal_conflict += 2;
                    }
                }
            }
        }

        return linear_vertical_conflict + linear_horizontal_conflict;
    }

    void calc_aggregate_costs() {
        /*
        Calculate the cumulative costs for an entire puzzle state. This is to give us an estimate on whether the move
        we are making will be getting us closer to our goal state or not.
        */
        this.h = 0;

        // loop over the state and add up each nodes f, g, and h costs
        for (int i = 0; i < this.state.size(); i++) {
            int node = this.state.get(i);
            if (node != 0) {
                this.h += this.calc_manhattan(i, node);
            }
        }

        this.h += this.calc_linear_conflict();
        this.f = this.g + this.h;
    }
}
