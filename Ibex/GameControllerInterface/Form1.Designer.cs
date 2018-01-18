

namespace GameControllerInterface
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.tbox_status = new System.Windows.Forms.TextBox();
            this.lbl_status = new System.Windows.Forms.Label();
            this.tbox_ipaddr = new System.Windows.Forms.TextBox();
            this.lbl_ipaddr = new System.Windows.Forms.Label();
            this.tabControl = new System.Windows.Forms.TabControl();
            this.page_action_queue = new System.Windows.Forms.TabPage();
            this.btn_start_queue = new System.Windows.Forms.Button();
            this.lbox_action_queue_list = new System.Windows.Forms.ListBox();
            this.page_drive = new System.Windows.Forms.TabPage();
            this.panel3 = new System.Windows.Forms.Panel();
            this.lbl_aq_drive_time = new System.Windows.Forms.Label();
            this.tbox_aq_drive_time = new System.Windows.Forms.TextBox();
            this.lbl_new_right_drive_setpoint = new System.Windows.Forms.Label();
            this.tbox_new_right_drive_setpoint = new System.Windows.Forms.TextBox();
            this.lbl_new_left_drive_setpoint = new System.Windows.Forms.Label();
            this.lbl_drive_motor_action_queue = new System.Windows.Forms.Label();
            this.tbox_new_left_drive_setpoint = new System.Windows.Forms.TextBox();
            this.btn_drive_setpoints = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.lbl_drive_motor_info = new System.Windows.Forms.Label();
            this.lbl_left_drive_setpoint = new System.Windows.Forms.Label();
            this.tbox_drive_LF_setpoint = new System.Windows.Forms.TextBox();
            this.tbox_drive_FL_reported = new System.Windows.Forms.TextBox();
            this.lbl_left_drive_reported = new System.Windows.Forms.Label();
            this.tbox_drive_RL_setpoint = new System.Windows.Forms.TextBox();
            this.lbl_right_drive_setpoint = new System.Windows.Forms.Label();
            this.tbox_drive_RL_reported = new System.Windows.Forms.TextBox();
            this.lbl_right_drive_reported = new System.Windows.Forms.Label();
            this.page_collector = new System.Windows.Forms.TabPage();
            this.page_config = new System.Windows.Forms.TabPage();
            this.timer_action_queue = new System.Windows.Forms.Timer(this.components);
            this.tabControl.SuspendLayout();
            this.page_action_queue.SuspendLayout();
            this.page_drive.SuspendLayout();
            this.panel3.SuspendLayout();
            this.panel1.SuspendLayout();
            this.page_config.SuspendLayout();
            this.SuspendLayout();
            // 
            // tbox_status
            // 
            this.tbox_status.Location = new System.Drawing.Point(98, 439);
            this.tbox_status.Multiline = true;
            this.tbox_status.Name = "tbox_status";
            this.tbox_status.ReadOnly = true;
            this.tbox_status.Size = new System.Drawing.Size(674, 112);
            this.tbox_status.TabIndex = 0;
            // 
            // lbl_status
            // 
            this.lbl_status.AutoSize = true;
            this.lbl_status.Font = new System.Drawing.Font("Microsoft Sans Serif", 16F);
            this.lbl_status.Location = new System.Drawing.Point(12, 439);
            this.lbl_status.Name = "lbl_status";
            this.lbl_status.Size = new System.Drawing.Size(80, 26);
            this.lbl_status.TabIndex = 1;
            this.lbl_status.Text = "Status:";
            // 
            // tbox_ipaddr
            // 
            this.tbox_ipaddr.Font = new System.Drawing.Font("Microsoft Sans Serif", 16F);
            this.tbox_ipaddr.Location = new System.Drawing.Point(193, 6);
            this.tbox_ipaddr.Name = "tbox_ipaddr";
            this.tbox_ipaddr.Size = new System.Drawing.Size(553, 32);
            this.tbox_ipaddr.TabIndex = 2;
            this.tbox_ipaddr.Text = "192.168.0.102";
            // 
            // lbl_ipaddr
            // 
            this.lbl_ipaddr.AutoSize = true;
            this.lbl_ipaddr.Font = new System.Drawing.Font("Microsoft Sans Serif", 16F);
            this.lbl_ipaddr.Location = new System.Drawing.Point(3, 9);
            this.lbl_ipaddr.Name = "lbl_ipaddr";
            this.lbl_ipaddr.Size = new System.Drawing.Size(184, 26);
            this.lbl_ipaddr.TabIndex = 3;
            this.lbl_ipaddr.Text = "IP Adresss of RPi";
            // 
            // tabControl
            // 
            this.tabControl.Controls.Add(this.page_action_queue);
            this.tabControl.Controls.Add(this.page_drive);
            this.tabControl.Controls.Add(this.page_collector);
            this.tabControl.Controls.Add(this.page_config);
            this.tabControl.Location = new System.Drawing.Point(12, 12);
            this.tabControl.Name = "tabControl";
            this.tabControl.SelectedIndex = 0;
            this.tabControl.Size = new System.Drawing.Size(760, 424);
            this.tabControl.TabIndex = 4;
            // 
            // page_action_queue
            // 
            this.page_action_queue.Controls.Add(this.btn_start_queue);
            this.page_action_queue.Controls.Add(this.lbox_action_queue_list);
            this.page_action_queue.Location = new System.Drawing.Point(4, 22);
            this.page_action_queue.Name = "page_action_queue";
            this.page_action_queue.Padding = new System.Windows.Forms.Padding(3);
            this.page_action_queue.Size = new System.Drawing.Size(752, 398);
            this.page_action_queue.TabIndex = 2;
            this.page_action_queue.Text = "Action Queue";
            this.page_action_queue.UseVisualStyleBackColor = true;
            // 
            // btn_start_queue
            // 
            this.btn_start_queue.Location = new System.Drawing.Point(6, 354);
            this.btn_start_queue.Name = "btn_start_queue";
            this.btn_start_queue.Size = new System.Drawing.Size(236, 36);
            this.btn_start_queue.TabIndex = 1;
            this.btn_start_queue.Text = "Start Action Queue";
            this.btn_start_queue.UseVisualStyleBackColor = true;
            this.btn_start_queue.Click += new System.EventHandler(this.btn_start_queue_Click);
            // 
            // lbox_action_queue_list
            // 
            this.lbox_action_queue_list.FormattingEnabled = true;
            this.lbox_action_queue_list.Location = new System.Drawing.Point(6, 6);
            this.lbox_action_queue_list.Name = "lbox_action_queue_list";
            this.lbox_action_queue_list.Size = new System.Drawing.Size(236, 342);
            this.lbox_action_queue_list.TabIndex = 0;
            // 
            // page_drive
            // 
            this.page_drive.Controls.Add(this.panel3);
            this.page_drive.Controls.Add(this.panel1);
            this.page_drive.Location = new System.Drawing.Point(4, 22);
            this.page_drive.Name = "page_drive";
            this.page_drive.Padding = new System.Windows.Forms.Padding(3);
            this.page_drive.Size = new System.Drawing.Size(752, 398);
            this.page_drive.TabIndex = 0;
            this.page_drive.Text = "Drive";
            this.page_drive.UseVisualStyleBackColor = true;
            // 
            // panel3
            // 
            this.panel3.Controls.Add(this.lbl_aq_drive_time);
            this.panel3.Controls.Add(this.tbox_aq_drive_time);
            this.panel3.Controls.Add(this.lbl_new_right_drive_setpoint);
            this.panel3.Controls.Add(this.tbox_new_right_drive_setpoint);
            this.panel3.Controls.Add(this.lbl_new_left_drive_setpoint);
            this.panel3.Controls.Add(this.lbl_drive_motor_action_queue);
            this.panel3.Controls.Add(this.tbox_new_left_drive_setpoint);
            this.panel3.Controls.Add(this.btn_drive_setpoints);
            this.panel3.Location = new System.Drawing.Point(6, 144);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(400, 216);
            this.panel3.TabIndex = 34;
            // 
            // lbl_aq_drive_time
            // 
            this.lbl_aq_drive_time.AutoSize = true;
            this.lbl_aq_drive_time.Location = new System.Drawing.Point(82, 89);
            this.lbl_aq_drive_time.Name = "lbl_aq_drive_time";
            this.lbl_aq_drive_time.Size = new System.Drawing.Size(55, 13);
            this.lbl_aq_drive_time.TabIndex = 51;
            this.lbl_aq_drive_time.Text = "Time (ms):";
            // 
            // tbox_aq_drive_time
            // 
            this.tbox_aq_drive_time.Location = new System.Drawing.Point(143, 86);
            this.tbox_aq_drive_time.Name = "tbox_aq_drive_time";
            this.tbox_aq_drive_time.Size = new System.Drawing.Size(191, 20);
            this.tbox_aq_drive_time.TabIndex = 50;
            this.tbox_aq_drive_time.Text = "1000";
            // 
            // lbl_new_right_drive_setpoint
            // 
            this.lbl_new_right_drive_setpoint.AutoSize = true;
            this.lbl_new_right_drive_setpoint.Location = new System.Drawing.Point(9, 63);
            this.lbl_new_right_drive_setpoint.Name = "lbl_new_right_drive_setpoint";
            this.lbl_new_right_drive_setpoint.Size = new System.Drawing.Size(130, 13);
            this.lbl_new_right_drive_setpoint.TabIndex = 37;
            this.lbl_new_right_drive_setpoint.Text = "New Right Drive Setpoint:";
            // 
            // tbox_new_right_drive_setpoint
            // 
            this.tbox_new_right_drive_setpoint.Location = new System.Drawing.Point(143, 60);
            this.tbox_new_right_drive_setpoint.Name = "tbox_new_right_drive_setpoint";
            this.tbox_new_right_drive_setpoint.Size = new System.Drawing.Size(191, 20);
            this.tbox_new_right_drive_setpoint.TabIndex = 36;
            this.tbox_new_right_drive_setpoint.Text = "90";
            // 
            // lbl_new_left_drive_setpoint
            // 
            this.lbl_new_left_drive_setpoint.AutoSize = true;
            this.lbl_new_left_drive_setpoint.Location = new System.Drawing.Point(16, 37);
            this.lbl_new_left_drive_setpoint.Name = "lbl_new_left_drive_setpoint";
            this.lbl_new_left_drive_setpoint.Size = new System.Drawing.Size(123, 13);
            this.lbl_new_left_drive_setpoint.TabIndex = 35;
            this.lbl_new_left_drive_setpoint.Text = "New Left Drive Setpoint:";
            // 
            // lbl_drive_motor_action_queue
            // 
            this.lbl_drive_motor_action_queue.AutoSize = true;
            this.lbl_drive_motor_action_queue.Font = new System.Drawing.Font("Microsoft Sans Serif", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_drive_motor_action_queue.Location = new System.Drawing.Point(3, 2);
            this.lbl_drive_motor_action_queue.Name = "lbl_drive_motor_action_queue";
            this.lbl_drive_motor_action_queue.Size = new System.Drawing.Size(265, 25);
            this.lbl_drive_motor_action_queue.TabIndex = 34;
            this.lbl_drive_motor_action_queue.Text = "Drive Motor Action Queue:";
            // 
            // tbox_new_left_drive_setpoint
            // 
            this.tbox_new_left_drive_setpoint.Location = new System.Drawing.Point(143, 34);
            this.tbox_new_left_drive_setpoint.Name = "tbox_new_left_drive_setpoint";
            this.tbox_new_left_drive_setpoint.Size = new System.Drawing.Size(191, 20);
            this.tbox_new_left_drive_setpoint.TabIndex = 1;
            this.tbox_new_left_drive_setpoint.Text = "90";
            // 
            // btn_drive_setpoints
            // 
            this.btn_drive_setpoints.Location = new System.Drawing.Point(3, 112);
            this.btn_drive_setpoints.Name = "btn_drive_setpoints";
            this.btn_drive_setpoints.Size = new System.Drawing.Size(331, 97);
            this.btn_drive_setpoints.TabIndex = 0;
            this.btn_drive_setpoints.Text = "Add To Action Queue";
            this.btn_drive_setpoints.UseVisualStyleBackColor = true;
            this.btn_drive_setpoints.Click += new System.EventHandler(this.btn_drive_setpoints_Click);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.lbl_drive_motor_info);
            this.panel1.Controls.Add(this.lbl_left_drive_setpoint);
            this.panel1.Controls.Add(this.tbox_drive_LF_setpoint);
            this.panel1.Controls.Add(this.tbox_drive_FL_reported);
            this.panel1.Controls.Add(this.lbl_left_drive_reported);
            this.panel1.Controls.Add(this.tbox_drive_RL_setpoint);
            this.panel1.Controls.Add(this.lbl_right_drive_setpoint);
            this.panel1.Controls.Add(this.tbox_drive_RL_reported);
            this.panel1.Controls.Add(this.lbl_right_drive_reported);
            this.panel1.Location = new System.Drawing.Point(6, 6);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(400, 132);
            this.panel1.TabIndex = 32;
            // 
            // lbl_drive_motor_info
            // 
            this.lbl_drive_motor_info.AutoSize = true;
            this.lbl_drive_motor_info.Font = new System.Drawing.Font("Microsoft Sans Serif", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lbl_drive_motor_info.Location = new System.Drawing.Point(4, 2);
            this.lbl_drive_motor_info.Name = "lbl_drive_motor_info";
            this.lbl_drive_motor_info.Size = new System.Drawing.Size(241, 25);
            this.lbl_drive_motor_info.TabIndex = 33;
            this.lbl_drive_motor_info.Text = "Drive Motor Information:";
            // 
            // lbl_left_drive_setpoint
            // 
            this.lbl_left_drive_setpoint.AutoSize = true;
            this.lbl_left_drive_setpoint.Location = new System.Drawing.Point(18, 33);
            this.lbl_left_drive_setpoint.Name = "lbl_left_drive_setpoint";
            this.lbl_left_drive_setpoint.Size = new System.Drawing.Size(98, 13);
            this.lbl_left_drive_setpoint.TabIndex = 1;
            this.lbl_left_drive_setpoint.Text = "Left Drive Setpoint:";
            // 
            // tbox_drive_LF_setpoint
            // 
            this.tbox_drive_LF_setpoint.Enabled = false;
            this.tbox_drive_LF_setpoint.Location = new System.Drawing.Point(117, 30);
            this.tbox_drive_LF_setpoint.Name = "tbox_drive_LF_setpoint";
            this.tbox_drive_LF_setpoint.Size = new System.Drawing.Size(90, 20);
            this.tbox_drive_LF_setpoint.TabIndex = 0;
            // 
            // tbox_drive_FL_reported
            // 
            this.tbox_drive_FL_reported.Enabled = false;
            this.tbox_drive_FL_reported.Location = new System.Drawing.Point(117, 56);
            this.tbox_drive_FL_reported.Name = "tbox_drive_FL_reported";
            this.tbox_drive_FL_reported.Size = new System.Drawing.Size(90, 20);
            this.tbox_drive_FL_reported.TabIndex = 2;
            // 
            // lbl_left_drive_reported
            // 
            this.lbl_left_drive_reported.AutoSize = true;
            this.lbl_left_drive_reported.Location = new System.Drawing.Point(13, 60);
            this.lbl_left_drive_reported.Name = "lbl_left_drive_reported";
            this.lbl_left_drive_reported.Size = new System.Drawing.Size(103, 13);
            this.lbl_left_drive_reported.TabIndex = 3;
            this.lbl_left_drive_reported.Text = "Left Drive Reported:";
            // 
            // tbox_drive_RL_setpoint
            // 
            this.tbox_drive_RL_setpoint.Enabled = false;
            this.tbox_drive_RL_setpoint.Location = new System.Drawing.Point(117, 81);
            this.tbox_drive_RL_setpoint.Name = "tbox_drive_RL_setpoint";
            this.tbox_drive_RL_setpoint.Size = new System.Drawing.Size(90, 20);
            this.tbox_drive_RL_setpoint.TabIndex = 8;
            // 
            // lbl_right_drive_setpoint
            // 
            this.lbl_right_drive_setpoint.AutoSize = true;
            this.lbl_right_drive_setpoint.Location = new System.Drawing.Point(11, 85);
            this.lbl_right_drive_setpoint.Name = "lbl_right_drive_setpoint";
            this.lbl_right_drive_setpoint.Size = new System.Drawing.Size(105, 13);
            this.lbl_right_drive_setpoint.TabIndex = 9;
            this.lbl_right_drive_setpoint.Text = "Right Drive Setpoint:";
            // 
            // tbox_drive_RL_reported
            // 
            this.tbox_drive_RL_reported.Enabled = false;
            this.tbox_drive_RL_reported.Location = new System.Drawing.Point(117, 107);
            this.tbox_drive_RL_reported.Name = "tbox_drive_RL_reported";
            this.tbox_drive_RL_reported.Size = new System.Drawing.Size(90, 20);
            this.tbox_drive_RL_reported.TabIndex = 10;
            // 
            // lbl_right_drive_reported
            // 
            this.lbl_right_drive_reported.AutoSize = true;
            this.lbl_right_drive_reported.Location = new System.Drawing.Point(6, 111);
            this.lbl_right_drive_reported.Name = "lbl_right_drive_reported";
            this.lbl_right_drive_reported.Size = new System.Drawing.Size(110, 13);
            this.lbl_right_drive_reported.TabIndex = 11;
            this.lbl_right_drive_reported.Text = "Right Drive Reported:";
            // 
            // page_collector
            // 
            this.page_collector.Location = new System.Drawing.Point(4, 22);
            this.page_collector.Name = "page_collector";
            this.page_collector.Padding = new System.Windows.Forms.Padding(3);
            this.page_collector.Size = new System.Drawing.Size(752, 398);
            this.page_collector.TabIndex = 3;
            this.page_collector.Text = "Collector";
            this.page_collector.UseVisualStyleBackColor = true;
            // 
            // page_config
            // 
            this.page_config.Controls.Add(this.lbl_ipaddr);
            this.page_config.Controls.Add(this.tbox_ipaddr);
            this.page_config.Location = new System.Drawing.Point(4, 22);
            this.page_config.Name = "page_config";
            this.page_config.Padding = new System.Windows.Forms.Padding(3);
            this.page_config.Size = new System.Drawing.Size(752, 398);
            this.page_config.TabIndex = 1;
            this.page_config.Text = "Config";
            this.page_config.UseVisualStyleBackColor = true;
            // 
            // timer_action_queue
            // 
            this.timer_action_queue.Tick += new System.EventHandler(this.timer_action_queue_Tick);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(784, 561);
            this.Controls.Add(this.tabControl);
            this.Controls.Add(this.lbl_status);
            this.Controls.Add(this.tbox_status);
            this.Name = "Form1";
            this.Text = "Form1";
            this.tabControl.ResumeLayout(false);
            this.page_action_queue.ResumeLayout(false);
            this.page_drive.ResumeLayout(false);
            this.panel3.ResumeLayout(false);
            this.panel3.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.page_config.ResumeLayout(false);
            this.page_config.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.TextBox tbox_status;
        private System.Windows.Forms.Label lbl_status;
        private System.Windows.Forms.TextBox tbox_ipaddr;
        private System.Windows.Forms.Label lbl_ipaddr;
        private System.Windows.Forms.TabControl tabControl;
        private System.Windows.Forms.TabPage page_drive;
        private System.Windows.Forms.Label lbl_right_drive_reported;
        private System.Windows.Forms.TextBox tbox_drive_RL_reported;
        private System.Windows.Forms.Label lbl_right_drive_setpoint;
        private System.Windows.Forms.TextBox tbox_drive_RL_setpoint;
        private System.Windows.Forms.Label lbl_left_drive_reported;
        private System.Windows.Forms.TextBox tbox_drive_FL_reported;
        private System.Windows.Forms.Label lbl_left_drive_setpoint;
        private System.Windows.Forms.TextBox tbox_drive_LF_setpoint;
        private System.Windows.Forms.TabPage page_config;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Label lbl_drive_motor_info;
        private System.Windows.Forms.TabPage page_action_queue;
        private System.Windows.Forms.ListBox lbox_action_queue_list;
        private System.Windows.Forms.Timer timer_action_queue;
        private System.Windows.Forms.Button btn_start_queue;
        private System.Windows.Forms.TabPage page_collector;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.Label lbl_aq_drive_time;
        private System.Windows.Forms.TextBox tbox_aq_drive_time;
        private System.Windows.Forms.Label lbl_new_right_drive_setpoint;
        private System.Windows.Forms.TextBox tbox_new_right_drive_setpoint;
        private System.Windows.Forms.Label lbl_new_left_drive_setpoint;
        private System.Windows.Forms.Label lbl_drive_motor_action_queue;
        private System.Windows.Forms.TextBox tbox_new_left_drive_setpoint;
        private System.Windows.Forms.Button btn_drive_setpoints;
    }
}

