using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using SlimDX.DirectInput;

namespace GameControllerInterface
{
    static class Program
    {
        [STAThread]
        static void Main()
        {
            CommandMessage msgTest = new CommandMessage();
            UInt16[] testdata0 = { 0xABCD, 0x1234 };
            UInt16[] testdata1 = { 0xDEAD, 0xBEEF};
            byte[] msg = msgTest.formMessage(1, testdata0, 2);
            msg = msgTest.formMessage(1, testdata1, 2);

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Form1());
        }
    }
}
