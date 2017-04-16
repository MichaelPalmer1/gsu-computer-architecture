package com.michaelpalmer.lab9sqldata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TuringAwardDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TURING_DB";
    public static final String TABLE_NAME = "ACM_TURING_AWARD";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_SCHOOL = "school";
    public static final String COLUMN_ALIVE = "alive";
    public static final String COLUMN_CITATION = "citation";

    public static int VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME + " VARCHAR(255) PRIMARY KEY, " +
                    COLUMN_YEAR + " INT(4), " +
                    COLUMN_SCHOOL + " VARCHAR(255), " +
                    COLUMN_ALIVE + " INT(1), " +
                    COLUMN_CITATION + " TEXT" +
            ")";

    private static ArrayList<TuringAward> awards = new ArrayList<>();

    static {
        awards.add(new TuringAward(
                "Alan J. Perlis", 1966, "MIT", false,
                "For his influence in the area of advanced computer programming techniques and compiler construction"
        ));
        awards.add(new TuringAward(
                "Maurice Wilkes", 1967, "University of Cambridge", false,
                "Professor Wilkes is best known as the builder and designer of the EDSAC, the first computer with an " +
                        "internally stored program. Built in 1949, the EDSAC used a mercury delay line memory. He is " +
                        "also known as the author, with Wheeler and Gill, of a volume on \"Preparation of Programs " +
                        "for Electronic Digital Computers\" in 1951, in which program libraries were effectively " +
                        "introduced"
        ));
        awards.add(new TuringAward(
                "Richard Hamming", 1968, "University of Illinois at Urbana-Champaign", false,
                "For his work on numerical methods, automatic coding systems, and error-detecting and " +
                        "error-correcting codes"
        ));
        awards.add(new TuringAward(
                "Marvin Minsky", 1969, "Princeton University", false,
                "For his central role in creating, shaping, promoting, and advancing the field of artificial " +
                        "intelligence."
        ));
        awards.add(new TuringAward(
                "James H. Wilkinson", 1970, "Trinity College, Cambridge", false,
                "For his research in numerical analysis to facilitate the use of the high-speed digital computer, " +
                        "having received special recognition for his work in computations in linear algebra " +
                        "and \"backward\" error analysis"
        ));
        awards.add(new TuringAward(
                "John McCarthy", 1971, "California Institute of Technology", false,
                "McCarthy's lecture \"The Present State of Research on Artificial Intelligence\" is a topic that " +
                        "covers the area in which he has achieved considerable recognition for his work"
        ));
        awards.add(new TuringAward(
                "Edsger W. Dijkstra", 1972, "University of Amsterdam", false,
                "Edsger Dijkstra was a principal contributor in the late 1950s to the development of the ALGOL, a " +
                        "high level programming language which has become a model of clarity and mathematical rigor. " +
                        "He is one of the principal proponents of the science and art of programming languages in " +
                        "general, and has greatly contributed to our understanding of their structure, " +
                        "representation, and implementation. His fifteen years of publications extend from " +
                        "theoretical articles on graph theory to basic manuals, expository texts, and philosophical " +
                        "contemplations in the field of programming languages"
        ));
        awards.add(new TuringAward(
                "Charles W. Bachman", 1973, "Michigan State University", true,
                "For his outstanding contributions to database technology"
        ));
        awards.add(new TuringAward(
                "Donald E. Knuth", 1974, "California Institute of Technology", true,
                "For his major contributions to the analysis of algorithms and the design of " +
                        "programming languages, and in particular for his contributions to \"The Art of Computer " +
                        "Programming\" through his well-known books in a continuous series by this title"
        ));
        awards.add(new TuringAward(
                "Allen Newell", 1991, "Carnegie Mellon University", false,
                "In joint scientific efforts extending over twenty years, " +
                        "initially in collaboration with J. C. Shaw at the RAND Corporation, and subsequently with " +
                        "numerous faculty and student colleagues at Carnegie Mellon University, they have made basic " +
                        "contributions to artificial intelligence, the psychology of human cognition, and list " +
                        "processing"
        ));
        awards.add(new TuringAward(
                "Herbert A. Simon", 1991, "University of Chicago", false,
                "In joint scientific efforts extending over twenty years, " +
                        "initially in collaboration with J. C. Shaw at the RAND Corporation, and subsequently with " +
                        "numerous faculty and student colleagues at Carnegie Mellon University, they have made basic " +
                        "contributions to artificial intelligence, the psychology of human cognition, and list " +
                        "processing"
        ));
        awards.add(new TuringAward(
                "Michael O. Rabin", 1976, "Princeton University", true,
                "For their joint paper \"Finite Automata and Their Decision Problem,\" which introduced the idea " +
                        "of nondeterministic machines, which has proved to be an enormously valuable concept. Their " +
                        "(Scott & Rabin) classic paper has been a continuous source of inspiration for subsequent " +
                        "work in this field"
        ));
        awards.add(new TuringAward(
                "Dana S. Scott", 1976, "Princeton University", true,
                "For their joint paper \"Finite Automata and Their Decision Problem,\" which introduced the idea " +
                        "of nondeterministic machines, which has proved to be an enormously valuable concept. Their " +
                        "(Scott & Rabin) classic paper has been a continuous source of inspiration for subsequent " +
                        "work in this field"
        ));
        awards.add(new TuringAward(
                "John Backus", 1977, "Columbia University", false,
                "For profound, influential, and lasting contributions to the design of practical " +
                        "high-level programming systems, notably through his work on FORTRAN, and for seminal " +
                        "publication of formal procedures for the specification of programming languages"
        ));
        awards.add(new TuringAward(
                "Robert W. Floyd", 1978, "University of Chicago", false,
                "For having a clear influence on methodologies for the creation of efficient and " +
                        "reliable software, and for helping to found the following important subfields of computer " +
                        "science: the theory of parsing, the semantics of programming languages, automatic program " +
                        "verification, automatic program synthesis, and analysis of algorithms[24]"
        ));
        awards.add(new TuringAward(
                "Kenneth E. Iverson", 1979, "Harvard University", false,
                "For his pioneering effort in programming languages and mathematical notation resulting in what the " +
                        "computing field now knows as APL, for his contributions to the implementation of " +
                        "interactive systems, to educational uses of APL, and to programming language theory and " +
                        "practice[25]"
        ));
        awards.add(new TuringAward(
                "Tony Hoare", 1980, "Moscow State University", false,
                "For his fundamental contributions to the definition and design of programming languages[26]"
        ));
        awards.add(new TuringAward(
                "Edgar F. Codd", 1981, "University of Michigan", false,
                "For his fundamental and continuing contributions to the theory and practice of database management " +
                        "systems, esp. relational databases[27]"
        ));
        awards.add(new TuringAward(
                "Stephen A. Cook", 1982, "University of Michigan", true,
                "For his advancement of our understanding of the complexity of computation in a significant and " +
                        "profound way[28]"
        ));
        awards.add(new TuringAward(
                "Ken Thompson", 1983, "University of California, Berkeley", true,
                "For their development of generic operating systems theory and " +
                        "specifically for the implementation of the UNIX operating system"
        ));
        awards.add(new TuringAward(
                "Dennis M. Ritchie", 1983, "Harvard University", false,
                "For their development of generic operating systems theory and " +
                        "specifically for the implementation of the UNIX operating system"
        ));
        awards.add(new TuringAward(
                "Niklaus Wirth", 1984, "University of California, Berkeley", true,
                "For developing a sequence of innovative computer languages, EULER, ALGOL-W, MODULA and Pascal"
        ));
        awards.add(new TuringAward(
                "Richard M. Karp", 1985, "Harvard University", true,
                "For his continuing contributions to the theory of algorithms including the " +
                        "development of efficient algorithms for network flow and other combinatorial optimization " +
                        "problems, the identification of polynomial-time computability with the intuitive notion of " +
                        "algorithmic efficiency, and, most notably, contributions to the theory of NP-completeness"
        ));
        awards.add(new TuringAward(
                "John Hopcroft", 1986, "Stanford University", true,
                "For fundamental achievements in the design and analysis of " +
                        "algorithms and data structures"
        ));
        awards.add(new TuringAward(
                "Robert Tarjan", 1986, "Stanford University", true,
                "For fundamental achievements in the design and analysis of " +
                        "algorithms and data structures"
        ));
        awards.add(new TuringAward(
                "John Cocke", 1987, "Duke University", false,
                "For significant contributions in the design and theory of compilers, the architecture of large " +
                        "systems and the development of reduced instruction set computers (RISC)"
        ));
        awards.add(new TuringAward(
                "Ivan Sutherland", 1988, "Carnegie Institute of Technology", true,
                "For his pioneering and visionary contributions to computer graphics, starting with Sketchpad, and " +
                        "continuing after"
        ));
        awards.add(new TuringAward(
                "William Kahan", 1989, "University of Toronto", true,
                "For his fundamental contributions to numerical analysis. One of the foremost experts " +
                        "on floating-point computations. Kahan has dedicated himself to \"making the world safe for " +
                        "numerical computations.\""
        ));
        awards.add(new TuringAward(
                "Fernando J. Corbató", 1990, "MIT", true,
                "For his pioneering work organizing the concepts and leading the development of " +
                        "the general-purpose, large-scale, time-sharing and resource-sharing computer systems, CTSS " +
                        "and Multics."
        ));
        awards.add(new TuringAward(
                "Robin Milner", 1991, "King's College, Cambridge", false,
                "For three distinct and complete achievements: 1) LCF, the mechanization of Scott's " +
                        "Logic of Computable Functions, probably the first theoretically based yet practical tool " +
                        "for machine assisted proof construction; 2) ML, the first language to include polymorphic " +
                        "type inference together with a type-safe exception-handling mechanism; 3) CCS, a general " +
                        "theory of concurrency. In addition, he formulated and strongly advanced full abstraction, " +
                        "the study of the relationship between operational and denotational semantics.[29]"
        ));
        awards.add(new TuringAward(
                "Butler W. Lampson", 1992, "University of California, Berkeley", true,
                "For contributions to the development of distributed, personal computing environments and the " +
                        "technology for their implementation: workstations, networks, operating systems, programming " +
                        "systems, displays, security and document publishing."
        ));
        awards.add(new TuringAward(
                "Juris Hartmanis", 1993, "California Institute of Technology", true,
                "In recognition of their seminal paper which established the foundations for the field of " +
                        "computational complexity theory.[30]"
        ));
        awards.add(new TuringAward(
                "Richard E. Stearns", 1993, "Princeton University", true,
                "In recognition of their seminal paper which established the foundations for the field of " +
                        "computational complexity theory.[30]"
        ));
        awards.add(new TuringAward(
                "Edward Feigenbaum", 1994, "Carnegie Mellon University", true,
                "For pioneering the design and construction of large scale artificial intelligence systems, " +
                        "demonstrating the practical importance and potential commercial impact of artificial intelligence technology.[31]"
        ));
        awards.add(new TuringAward(
                "Raj Reddy", 1994, "Stanford University", true,
                "For pioneering the design and construction of large scale artificial intelligence systems, " +
                        "demonstrating the practical importance and potential commercial impact of artificial intelligence technology.[31]"
        ));
        awards.add(new TuringAward(
                "Manuel Blum", 1995, "MIT", true,
                "In recognition of his contributions to the foundations of computational complexity theory and its " +
                        "application to cryptography and program checking."
        ));
        awards.add(new TuringAward(
                "Amir Pnueli", 1996, "Weizmann Institute of Science", false,
                "For seminal work introducing temporal logic into computing science and for outstanding " +
                        "contributions to program and systems verification."
        ));
        awards.add(new TuringAward(
                "Douglas Engelbart", 1997, "University of California, Berkeley", false,
                "For an inspiring vision of the future of interactive computing and the invention of key " +
                        "technologies to help realize this vision."
        ));
        awards.add(new TuringAward(
                "Jim Gray", 1998, "University of California, Berkeley", false,
                "For seminal contributions to database and transaction processing research and technical leadership " +
                        "in system implementation."
        ));
        awards.add(new TuringAward(
                "Frederick P. Brooks, Jr.", 1999, "Harvard University", true,
                "For landmark contributions to computer architecture, operating systems, and software engineering."
        ));
        awards.add(new TuringAward(
                "Andrew Chi-Chih Yao", 2000, "University of Illinois at Urbana–Champaign", true,
                "In recognition of his fundamental contributions to the theory of computation, " +
                        "including the complexity-based theory of pseudorandom number generation, cryptography, and " +
                        "communication complexity."
        ));
        awards.add(new TuringAward(
                "Ole-Johan Dahl", 2001, "", false,
                "For ideas fundamental to the emergence of object-oriented " +
                        "programming, through their design of the programming languages Simula I and Simula 67."
        ));
        awards.add(new TuringAward(
                "Kristen Nygaard", 2001, "University of Oslo", false,
                "For ideas fundamental to the emergence of object-oriented " +
                        "programming, through their design of the programming languages Simula I and Simula 67."
        ));
        awards.add(new TuringAward(
                "Ronald L. Rivest", 2002, "Yale University", true,
                "For their ingenious contribution for making public-key cryptography useful in practice."
        ));
        awards.add(new TuringAward(
                "Adi Shamir", 2002, "Weizmann Institute of Science", true,
                "For their ingenious contribution for making public-key cryptography useful in practice."
        ));
        awards.add(new TuringAward(
                "Leonard M. Adleman", 2002, "University of California, Berkeley", true,
                "For their ingenious contribution for making public-key cryptography useful in practice."
        ));
        awards.add(new TuringAward(
                "Alan Kay", 2003, "University of Utah", true,
                "For pioneering many of the ideas at the root of contemporary object-oriented programming languages, " +
                        "leading the team that developed Smalltalk, and for fundamental contributions to personal " +
                        "computing."
        ));
        awards.add(new TuringAward(
                "Vinton G. Cerf", 2004, "UCLA", true,
                "For pioneering work on internetworking, including the design and implementation of the Internet's " +
                        "basic communications protocols, TCP/IP, and for inspired leadership in networking."
        ));
        awards.add(new TuringAward(
                "Robert E. Kahn", 2004, "Princeton University", true,
                "For pioneering work on internetworking, including the design and implementation of the Internet's " +
                        "basic communications protocols, TCP/IP, and for inspired leadership in networking."
        ));
        awards.add(new TuringAward(
                "Peter Naur", 2005, "", false,
                "For fundamental contributions to programming language design and the definition of ALGOL 60, to " +
                        "compiler design, and to the art and practice of computer programming."
        ));
        awards.add(new TuringAward(
                "Frances E. Allen", 2006, "University of Michigan", true,
                "For pioneering contributions to the theory and practice of optimizing compiler techniques that laid " +
                        "the foundation for modern optimizing compilers and automatic parallel execution."
        ));
        awards.add(new TuringAward(
                "Edmund M. Clarke", 2007, "Cornell University", true,
                "For their roles in developing model checking into a highly effective verification technology, " +
                        "widely adopted in the hardware and software industries.[32]"
        ));
        awards.add(new TuringAward(
                "E. Allen Emerson", 2007, "U. of Texas, Harvard", true,
                "For their roles in developing model checking into a highly effective verification technology, " +
                        "widely adopted in the hardware and software industries.[32]"
        ));
        awards.add(new TuringAward(
                "Joseph Sifakis", 2007, "University of Grenoble", true,
                "For their roles in developing model checking into a highly effective verification technology, " +
                        "widely adopted in the hardware and software industries.[32]"
        ));
        awards.add(new TuringAward(
                "Barbara Liskov", 2008, "Stanford University", true,
                "For contributions to practical and theoretical foundations of programming language and system " +
                        "design, especially related to data abstraction, fault tolerance, and distributed computing."
        ));
        awards.add(new TuringAward(
                "Charles P. Thacker", 2009, "University of California, Berkeley", true,
                "For his pioneering design and realization of the Xerox Alto, the first modern personal computer, " +
                        "and in addition for his contributions to the Ethernet and the Tablet PC."
        ));
        awards.add(new TuringAward(
                "Leslie G. Valiant", 2010, "University of Warwick", true,
                "For transformative contributions to the theory of computation, including the theory of probably " +
                        "approximately correct (PAC) learning, the complexity of enumeration and of algebraic " +
                        "computation, and the theory of parallel and distributed computing."
        ));
        awards.add(new TuringAward(
                "Judea Pearl", 2011, "New York University Tandon School of Engineering", true,
                "For fundamental contributions to artificial intelligence through the development " +
                        "of a calculus for probabilistic and causal reasoning.[34]"
        ));
        awards.add(new TuringAward(
                "Silvio Micali", 2012, "University of California, Berkeley", true,
                "For transformative work that laid the complexity-theoretic " +
                        "foundations for the science of cryptography and in the process pioneered new methods for " +
                        "efficient verification of mathematical proofs in complexity theory.[35]"
        ));
        awards.add(new TuringAward(
                "Shafi Goldwasser", 2012, "University of California, Berkeley", true,
                "For transformative work that laid the complexity-theoretic " +
                        "foundations for the science of cryptography and in the process pioneered new methods for " +
                        "efficient verification of mathematical proofs in complexity theory.[35]"
        ));
        awards.add(new TuringAward(
                "Leslie Lamport", 2013, "Brandeis University", true,
                "For fundamental contributions to the theory and practice of distributed and concurrent systems, " +
                        "notably the invention of concepts such as causality and logical clocks, safety and " +
                        "liveness, replicated state machines, and sequential consistency.[36][37]"
        ));
        awards.add(new TuringAward(
                "Michael Stonebraker", 2014, "University of Michigan", true,
                "For fundamental contributions to the concepts and practices underlying modern database systems.[38]"
        ));
        awards.add(new TuringAward(
                "Martin E. Hellman", 2015, "Stanford University", true,
                "For fundamental contributions to modern cryptography. Diffie and Hellman's groundbreaking 1976 " +
                        "paper, \"New Directions in Cryptography,\"[39] introduced the ideas of public-key " +
                        "cryptography and digital signatures, which are the foundation for most regularly-used " +
                        "security protocols on the internet today.[40]"
        ));
        awards.add(new TuringAward(
                "Whitfield Diffie", 2015, "MIT", true,
                "For fundamental contributions to modern cryptography. Diffie and Hellman's groundbreaking 1976 " +
                        "paper, \"New Directions in Cryptography,\"[39] introduced the ideas of public-key " +
                        "cryptography and digital signatures, which are the foundation for most regularly-used " +
                        "security protocols on the internet today.[40]"
        ));
        awards.add(new TuringAward(
                "Tim Berners-Lee", 2016, "The Queen's College, Oxford", true,
                "For inventing the World Wide Web, the first web browser, and the fundamental protocols and " +
                        "algorithms allowing the Web to scale."
        ));

    }

    public TuringAwardDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    ArrayList<TuringAward> getAliveRecipients() {
        ArrayList<TuringAward> awards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_NAME, COLUMN_YEAR, COLUMN_SCHOOL, COLUMN_ALIVE, COLUMN_CITATION},
                COLUMN_ALIVE + " = 1",
                null, null, null, null
        );

        while (cursor.moveToNext()) {
            awards.add(new TuringAward(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ALIVE)) == 1,
                    cursor.getString(cursor.getColumnIndex(COLUMN_CITATION))
            ));
        }
        cursor.close();
        db.close();

        return awards;
    }

    ArrayList<TuringAward> recipientsFromSchool(String school) {
        ArrayList<TuringAward> awards = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_NAME, COLUMN_YEAR, COLUMN_SCHOOL, COLUMN_ALIVE, COLUMN_CITATION},
                COLUMN_SCHOOL + "=?",
                new String[]{school},
                null, null, null
        );

        while (cursor.moveToNext()) {
            awards.add(new TuringAward(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_SCHOOL)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ALIVE)) == 1,
                    cursor.getString(cursor.getColumnIndex(COLUMN_CITATION))
            ));
        }
        cursor.close();
        db.close();

        return awards;
    }

    public void populateData() {
        SQLiteDatabase db = getWritableDatabase();

        for (TuringAward turingAward : awards) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, turingAward.getName());
            values.put(COLUMN_YEAR, turingAward.getYear());
            values.put(COLUMN_SCHOOL, turingAward.getSchool());
            values.put(COLUMN_ALIVE, turingAward.isAlive() ? 1 : 0);
            values.put(COLUMN_CITATION, turingAward.getCitation());

            db.insert(
                    TABLE_NAME,
                    COLUMN_NAME + ", " + COLUMN_YEAR + ", " + COLUMN_SCHOOL + ", " +
                            COLUMN_ALIVE + ", " + COLUMN_CITATION,
                    values
            );
        }

        db.close();
    }
}
