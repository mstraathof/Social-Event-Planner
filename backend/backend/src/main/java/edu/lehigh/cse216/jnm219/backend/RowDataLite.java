package edu.lehigh.cse216.jnm219.backend;

 public class RowDataLite {
        /**
        * The id for this row; see DataRow.mId
        */
        public int mId;

        /**
         * The title string for this row of data; see DataRow.mTitle
        */
        public String mSubject;
        /**
         * The number of votes for this row
         */
        public int mVotes;

        /**
         * Create a DataRowLite by copying fields from a DataRow
        */
        public RowDataLite(RowData data) {
            this.mId = data.mId;
            this.mSubject = data.mSubject;
            this.mVotes = data.mVotes;
        }
    }